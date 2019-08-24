package hackathon.co.kr.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import hackathon.co.kr.neopen.R;
import hackathon.co.kr.neopen.databinding.ActivityMainBinding;
import hackathon.co.kr.ui.fragment.FourFragment;
import hackathon.co.kr.ui.fragment.OneFragment;
import hackathon.co.kr.ui.fragment.ThreeFragment;
import hackathon.co.kr.ui.fragment.TwoFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    ActivityMainBinding binding;

    private OneFragment oneFragment = OneFragment.newInstance();
    private TwoFragment twoFragment = TwoFragment.newInstance();
    private ThreeFragment threeFragment = ThreeFragment.newInstance();
    private FourFragment fourFragment = FourFragment.newInstance();

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private Fragment active = oneFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.navBottomnavigation.setOnNavigationItemSelectedListener(this);

        fragmentManager.beginTransaction().add(R.id.container, oneFragment).commit();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.one:
                showFragment(oneFragment);
                return true;
            case R.id.two:
                showFragment(twoFragment);
                return true;
            case R.id.three:
                showFragment(threeFragment);
                return true;
            case R.id.four:
                showFragment(fourFragment);
                return true;

        }
        return false;
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        if (fragmentManager.getFragments().contains(fragment)) {
            fragmentTransaction.hide(active).show(fragment).commit();
        }
        else {
            fragmentTransaction.add(R.id.container, fragment).hide(active).commit();
        }
        active = fragment;
    }

    @Override
    public void onBackPressed() {

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment != null && fragment.isVisible()) {
                if (fragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
                    fragment.getChildFragmentManager().popBackStack();
                    return;
                }
                else if (!(fragment instanceof OneFragment)) {
                    binding.navBottomnavigation.setSelectedItemId(R.id.home);
                    return;
                }
            }
        }
        super.onBackPressed();
    }

}
