package vn.edu.usth.wikiapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPageAccountAdapter extends FragmentStateAdapter {

    public ViewPageAccountAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new EditUsernameFragment();
            case 1:
                return new ChangePasswordFragment();
            case 2:
                return new DeleteAccountFragment();
            default:
                return new EditUsernameFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
