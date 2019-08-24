package hackathon.co.kr.neopen.sdk.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import java.util.Locale;

import hackathon.co.kr.neopen.sdk.ink.structure.Dot;
import hackathon.co.kr.neopen.sdk.ink.structure.Stroke;
import hackathon.co.kr.neopen.sdk.metadata.MetadataCtrl;
import hackathon.co.kr.neopen.sdk.renderer.Renderer;

public class StrokeUtil
{
    public static Bitmap StrokeToImage( Stroke[] strokes, float scale)
    {
        float minX, minY, maxX, maxY, offsetX, offsetY ;
        int width, height;

        if( strokes == null || strokes.length == 0 )
            return null;

        Dot fd = strokes[0].getDots().get( 0 );
        minX = fd.getX();
        minY = fd.getY();
        maxX = (int)fd.getX();
        maxY = (int)fd.getY();

        // get strokes offset, width, height
        for( Stroke stroke : strokes )
        {
            for( Dot d : stroke.getDots() )
            {
                // get min X
                if( minX > d.getX() )
                    minX = d.getX();

                // get min Y
                if( minY > d.getY() )
                    minY = d.getY();


                // get max X
                if( maxX < d.getX() )
                    maxX = (int)d.getX();

                // get max Y
                if( maxY < d.getY() )
                    maxY = (int)d.getY();
            }
        }

        minX *= scale;
        minY *= scale;
        maxX *= scale;
        maxY *= scale;

        // 반올림
        width = (int)((maxX - minX) + 0.5f);
        height = (int)((maxY - minY) + 0.5f);

        offsetX = minX;
        offsetY = minY;

        // 여백 만들기
        width = width + 25;
        height = height + 25;
        offsetX = offsetX - 7;
        offsetY = offsetY - 7;

        Bitmap bitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
        bitmap.eraseColor( Color.WHITE );
        Canvas canvas = new Canvas(bitmap);

        Renderer.draw( canvas, strokes, scale, -offsetX, -offsetY, Stroke.STROKE_TYPE_PEN );

        return bitmap;
    }

    public static String StrokeToNeoInk( String captureDevice, Stroke[] strokes )
    {
        if(strokes == null || strokes.length <= 0)
            return "";

        Stroke stroke = strokes[0];
        StringBuilder builder = new StringBuilder();

        builder.append( deviceToNeoInk( "1.0", captureDevice ));
        builder.append( pageToNeoInk( stroke.sectionId, stroke.ownerId, stroke.noteId, stroke.pageId ) );

        for( Stroke s : strokes)
        {
            String strokeStr = String.format( Locale.getDefault(),
                                        "[\"%s\",\n"
                                                + " \"%s\",\n"
                                                + " \"#%06X\",\n"
                                                + " %.1f,\n"
                                                + " %d,\n",
                                        captureDevice,
                                        s.penTipType == 2 ? "highlight" : "solid",
                                        ( 0xFFFFFF & s.color ),
                                        (float) s.thickness,
                                        s.timeStampStart );

            if( s.getDots().size() > 0 )
                strokeStr += "[\n";


            builder.append( strokeStr );
            for ( Dot d : s.getDots() )
            {
                String dotStr = String.format( Locale.getDefault(),
                                               "%d,\n" +
                                                       "%.2f,\n" +
                                                       "%.2f,\n%.2f,\n" +
                                                       "%d,\n" +
                                                       "%d,\n",
                                               d.timestamp - s.timeStampStart,
                                               d.x,
                                               d.y,
                                               (float) d.pressure * 100f / 255f,
                                               d.tiltX,
                                               d.twist );

                builder.append( dotStr );
            }

            builder.deleteCharAt( builder.lastIndexOf( "," ) );
            builder.append( "],\n" );
        }

        builder.deleteCharAt( builder.lastIndexOf( "," ) );
        builder.append( "]\n]\n}" );

        return  builder.toString();
    }

    public static String deviceToNeoInk( String version, String captureDevice )
    {
        String deviceStr = String.format( Locale.getDefault(),
                                    "{\n\"Version\": \"%s\",\n" +
                                            "\"CaptureDevice\": [{\n" +
                                            "\"Id\": \"%s\"\n," +
                                            "\"Desc\": \"%s\"\n," +
                                            "\"SampleRate\": \"%d\"\n," +
                                            "\"Capability\": {\n" +
                                            "\"Tilt\":  \"false\"\n," +
                                            "\"Rotation\":  \"false\"\n}," +
                                            "\"Unit\": \"Inch\"\n" +
                                            "}],\n"
                                            ,
                                    version,
                                    captureDevice,
                                    "description" ,
                                    100 );

        return deviceStr;
    }

    public static String pageToNeoInk( int section, int owner, int noteId, int page)
    {
        MetadataCtrl ctrl = MetadataCtrl.getInstance();
        String title = ctrl.getTitle( noteId );
        float width = ctrl.getPageWidth( noteId, page );
        float height = ctrl.getPageHeight( noteId, page );
        float left = ctrl.getPageMarginLeft( noteId, page );
        float top = ctrl.getPageMarginTop( noteId, page );
        float right = ctrl.getPageMarginRight( noteId, page );
        float bottom = ctrl.getPageMarginBottom( noteId, page );

        float croppedWidth = ctrl.getPageWidthWithMargin( noteId, page );
        float croppedHeight = ctrl.getPageHeightWithMargin( noteId, page );

        String pageStr = String.format( Locale.getDefault(),
                                          "\"PageInfo\": {\n" +
                                                  "\"Desc\": %s\n" +
                                                  "\"PageNumber\": %d\n," +
                                                  "\"Unit\": \"%s\"\n," +
                                                  "\"Size\": [\n," +
                                                  "%.6f,\n" +
                                                  "%.6f,\n],\n" +
                                                  "\"CropMargin\": [\n," +
                                                  "%.7f,\n" + "%.7f,\n" +   // left, top
                                                  "%.7f,\n" + "%.7f,\n],\n" +   // right, bottom
                                                  "\"CroppedSize\":[\n" +
                                                  "%.6f\n," +
                                                  "%.6f\n]\n},",
                                          title,
                                          page,
                                          "mm",
                                        width,
                                        height,
                                        left , top,
                                        right, bottom,
                                        croppedWidth, croppedHeight );

        return pageStr;
    }
}
