package hackathon.co.kr.neopen.sdk.pen.bluetooth.cmd;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

import hackathon.co.kr.neopen.sdk.pen.bluetooth.comm.CommProcessor20;
import hackathon.co.kr.neopen.sdk.pen.bluetooth.lib.Chunk;
import hackathon.co.kr.neopen.sdk.pen.bluetooth.lib.ProtocolParser20;
import hackathon.co.kr.neopen.sdk.pen.penmsg.PenMsg;
import hackathon.co.kr.neopen.sdk.pen.penmsg.PenMsgType;
import hackathon.co.kr.neopen.sdk.util.NLog;

/**
 * The type Fw upgrade command 20.
 *
 * @author Moo
 */
public class FwUpgradeCommand20 extends Command
{
	private File source = null;

	private String fwVersion;
	private String deviceName;
	private boolean isCompress;

	/**
	 * The Packet size.
	 */
	public final int PEN_PACKET_SIZE = 2 * 1024;
//	static public final int PACKET_SIZE = 128;

	private int packetSize = PEN_PACKET_SIZE;

	private int count = 0;

	private boolean repeat = true;

	private int wait_timeout = 5000;

	private int wait = 10;

	/**
	 * Instantiates a new Fw upgrade command 20.
	 *
	 * @param key  the key
	 * @param comp the comp
	 */
	public FwUpgradeCommand20 ( int key, CommandManager comp )
	{
		super( key, comp );
	}

	/**
	 * Sets info.
	 * support protocol 2.0
	 *
	 * @param source     the source
	 * @param fwVersion  the fw version
	 * @param deviceName the device name
	 * @param isCompress the is compress
	 */
	public void setInfo( File source, String fwVersion,  String deviceName, boolean isCompress)
	{
		this.source = source;
		this.fwVersion = fwVersion;
		this.deviceName = deviceName;
		this.isCompress = isCompress;

		if(this.isCompress)
		{
			// E100,D100,C200,P201 uncompressed
			if(deviceName.equals( "NEP-E100" ) || deviceName.equals( "NSP-D100" ) || deviceName.equals( "NSP-D101" ) || deviceName.equals( "NSP-C200") || deviceName.equals( "NPP-P201" ))
				this.isCompress = false;
			else
				this.isCompress = true;
		}

		if(deviceName.equals( "NSP-D100" ) || deviceName.equals( "NSP-D101" ) || deviceName.equals( "NSP-C200") )
		{
			packetSize = 128;
		}
		else
			packetSize = PEN_PACKET_SIZE;

	}


	@Override
	protected void write()
	{
	}

	private void doUpgrade()
	{
		Chunk chunk = null;

		try
		{
			InputStream is = new FileInputStream( source );

			int filesize = (int) source.length();

			chunk = new Chunk( is, filesize,packetSize );

			chunk.load();

			comp.setChunk( chunk );

			byte[] datas = null;

			try
			{
				datas = ProtocolParser20.buildPenSwUpgrade( fwVersion,deviceName ,filesize, chunk.getChecksum() ,isCompress, packetSize);
				comp.write( datas );
			}
			catch ( Exception e )
			{
				NLog.e( "[FwUpgradeCommand20] can't write upgrade request packet.", e );
				comp.getConn().onCreateMsg( new PenMsg( PenMsgType.PEN_FW_UPGRADE_FAILURE ) );
				return;
			}
		}
		catch ( IOException e )
		{
			NLog.e( "[FwUpgradeCommand20] can't open firmware file.", e );
			comp.getConn().onCreateMsg( new PenMsg( PenMsgType.PEN_FW_UPGRADE_FAILURE ) );
			return;
		}

		Queue<CommProcessor20.FwPacketInfo> queue = ((CommProcessor20)comp).rQueue;

//		int index = 0;
		int timeout = wait_timeout / wait;
		int offset = 0;
		while ( repeat )
		{
//			NLog.e( "[FwUpgradeCommand20] queue="+queue.size());
			if ( !queue.isEmpty() )
			{
				CommProcessor20.FwPacketInfo info = (CommProcessor20.FwPacketInfo)queue.poll();
				// retry fw transfer
				count = 0;
				try
				{
					if(info.status == CommProcessor20.FwPacketInfo.STATUS_END)
					{
						comp.getConn().onCreateMsg( new PenMsg( PenMsgType.PEN_FW_UPGRADE_SUCCESS ) );
					}

					int index = chunk.offsetToIndex( info.offset );
					comp.write( ProtocolParser20.buildPenSwUploadChunk( info.offset, chunk.getChunk( index ), info.status , isCompress) );
					if(info.status == CommProcessor20.FwPacketInfo.STATUS_ERROR)
					{
						NLog.e( "[FwUpgradeCommand20] STATUS_ERROR");
						comp.getConn().onCreateMsg( new PenMsg( PenMsgType.PEN_FW_UPGRADE_FAILURE ) );
						comp.finishUpgrade();
						repeat = false;
						continue;
					}



					int maximum = chunk.getChunkLength();
					NLog.d( "[FwUpgradeCommand20] send progress => maximum : " + maximum + ", current : " + index );
					JSONObject job;
					try
					{
						job = new JSONObject();
						job.put( "total_size", maximum );
						job.put( "sent_size", index );
						
						comp.getConn().onCreateMsg( new PenMsg( PenMsgType.PEN_FW_UPGRADE_STATUS, job ) );
					}
					catch ( JSONException e )
					{
						e.printStackTrace();
					}
				}
				catch ( Exception e )
				{
					e.printStackTrace();
					try
					{
						if(info.status == CommProcessor20.FwPacketInfo.STATUS_ERROR)
						{
							NLog.e( "[FwUpgradeCommand20] can't write chunk packet.", e );
							comp.getConn().onCreateMsg( new PenMsg( PenMsgType.PEN_FW_UPGRADE_FAILURE ) );
							comp.finishUpgrade();
							repeat = false;
							continue;
						}
						int index = chunk.offsetToIndex( info.offset );
						comp.write( ProtocolParser20.buildPenSwUploadChunk( info.offset, chunk.getChunk( index ), info.status , isCompress) );

						int maximum = chunk.getChunkLength();
						NLog.d( "[FwUpgradeCommand20] send progress No Compress=> maximum : " + maximum + ", current : " + index );
						JSONObject job;
						try
						{
							job = new JSONObject();
							job.put( "total_size", maximum );
							job.put( "sent_size", index );

							comp.getConn().onCreateMsg( new PenMsg( PenMsgType.PEN_FW_UPGRADE_STATUS, job ) );
						}
						catch ( JSONException e3 )
						{
							e3.printStackTrace();
						}

					}catch ( Exception e2 )
					{
						NLog.e( "[FwUpgradeCommand20] can't write chunk packet.", e2 );
						comp.getConn().onCreateMsg( new PenMsg( PenMsgType.PEN_FW_UPGRADE_FAILURE ) );
						comp.finishUpgrade();
						repeat = false;
						continue;

					}

				}
			}
			if ( count >= timeout )
			{
				NLog.e( "[FwUpgradeCommand20] tracing : wait timeout.count="+count );
				comp.getConn().onCreateMsg( new PenMsg( PenMsgType.PEN_FW_UPGRADE_FAILURE ) );
				comp.finishUpgrade();
				repeat = false;

				continue;
			}

			try
			{
				Thread.sleep( wait );
			}
			catch ( Exception e )
			{
				e.printStackTrace();
			}
			count++;
		}
	}

	public void run()
	{
		this.doUpgrade();

		super.isAlive = false;
	}

	public void finish()
	{
		this.repeat = false;
	}



}
