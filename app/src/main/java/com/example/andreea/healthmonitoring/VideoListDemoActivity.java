package com.example.andreea.healthmonitoring;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnFullscreenListener;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.android.youtube.player.YouTubeThumbnailView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewPropertyAnimator;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VideoListDemoActivity extends Activity implements OnFullscreenListener {

    /**
     * The duration of the animation sliding up the video in portrait.
     */
    private static final int ANIMATION_DURATION_MILLIS = 300;
    /**
     * The padding between the video list and the video in landscape orientation.
     */
    private static final int LANDSCAPE_VIDEO_PADDING_DP = 5;

    /**
     * The request code when calling startActivityForResult to recover from an API service error.
     */
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private VideoListFragment listFragment;
    private VideoFragment videoFragment;

    private View videoBox;
    private View closeButton;

    private boolean isFullscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video_list_demo);

        listFragment = (VideoListFragment) getFragmentManager().findFragmentById(R.id.list_fragment);
        videoFragment =
                (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);

        videoBox = findViewById(R.id.video_box);
        closeButton = findViewById(R.id.close_button);

        videoBox.setVisibility(View.INVISIBLE);

        layout();

        checkYouTubeApi();
    }

    private void checkYouTubeApi() {
        YouTubeInitializationResult errorReason =
                YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else if (errorReason != YouTubeInitializationResult.SUCCESS) {
            String errorMessage =
                    String.format(getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Recreate the activity if user performed a recovery action
            recreate();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        layout();
    }

    @Override
    public void onFullscreen(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;

        layout();
    }

    /**
     * Sets up the layout programatically for the three different states. Portrait, landscape or
     * fullscreen+landscape. This has to be done programmatically because we handle the orientation
     * changes ourselves in order to get fluent fullscreen transitions, so the xml layout resources
     * do not get reloaded.
     */
    private void layout() {
        boolean isPortrait =
                getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        listFragment.getView().setVisibility(isFullscreen ? View.GONE : View.VISIBLE);
        listFragment.setLabelVisibility(isPortrait);
        closeButton.setVisibility(isPortrait ? View.VISIBLE : View.GONE);

        if (isFullscreen) {
            videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
            setLayoutSize(videoFragment.getView(), MATCH_PARENT, MATCH_PARENT);
            setLayoutSizeAndGravity(videoBox, MATCH_PARENT, MATCH_PARENT, Gravity.TOP | Gravity.LEFT);
        } else if (isPortrait) {
            setLayoutSize(listFragment.getView(), MATCH_PARENT, MATCH_PARENT);
            setLayoutSize(videoFragment.getView(), MATCH_PARENT, WRAP_CONTENT);
            setLayoutSizeAndGravity(videoBox, MATCH_PARENT, WRAP_CONTENT, Gravity.BOTTOM);
        } else {
            videoBox.setTranslationY(0); // Reset any translation that was applied in portrait.
            int screenWidth = dpToPx(getResources().getConfiguration().screenWidthDp);
            setLayoutSize(listFragment.getView(), screenWidth / 4, MATCH_PARENT);
            int videoWidth = screenWidth - screenWidth / 4 - dpToPx(LANDSCAPE_VIDEO_PADDING_DP);
            setLayoutSize(videoFragment.getView(), videoWidth, WRAP_CONTENT);
            setLayoutSizeAndGravity(videoBox, videoWidth, WRAP_CONTENT,
                    Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        }
    }

    public void onClickClose(@SuppressWarnings("unused") View view) {
        listFragment.getListView().clearChoices();
        listFragment.getListView().requestLayout();
        videoFragment.pause();
        ViewPropertyAnimator animator = videoBox.animate()
                .translationYBy(videoBox.getHeight())
                .setDuration(ANIMATION_DURATION_MILLIS);
        runOnAnimationEnd(animator, new Runnable() {
            @Override
            public void run() {
                videoBox.setVisibility(View.INVISIBLE);
            }
        });
    }

    @TargetApi(16)
    private void runOnAnimationEnd(ViewPropertyAnimator animator, final Runnable runnable) {
        if (Build.VERSION.SDK_INT >= 16) {
            animator.withEndAction(runnable);
        } else {
            animator.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    runnable.run();
                }
            });
        }
    }

    /**
     * A fragment that shows a static list of videos.
     */
    public static final class VideoListFragment extends ListFragment {

        private static final List<VideoEntry> VIDEO_LIST;

        static {
            List<VideoEntry> list = new ArrayList<VideoEntry>();
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);

            switch (day) {

                case Calendar.SUNDAY:
                    list.add(new VideoEntry("Work Out: Hip Hop Dance to Tone Abs | Danielle Peazer", "G9kFL_by5Nw"));
                    list.add(new VideoEntry("Work Out: Dance to Burn Fat | Danielle Peazer", "jszPZiqEheI"));
                    list.add(new VideoEntry("Easy Hip Hop Dance Tutorial | Danielle Peazer", "Hum3fSs8KDY"));
                    list.add(new VideoEntry("Dance Workout: Toned Legs I Danielle Peazer", "krC3PtDSq10"));
                    list.add(new VideoEntry("7 Minute Quick and Effective Workout | Danielle Peazer", "Lu1qJrjxzME"));
                    list.add(new VideoEntry("Workout: Squat Challenge | Danielle Peazer", "N_o0RspYLD0"));
                    list.add(new VideoEntry("4 STEPS TO FLAT ABS | Danielle Peazer", "ux-vQZVv79Y"));

                case Calendar.MONDAY:
                    list.add(new VideoEntry("Low Impact Cardio and Toning Workout for Beginners", "GjAhM651ZPU"));
                    list.add(new VideoEntry("Fat Burning Low Impact Cardio Workout at Home", "waPCCrftvQA"));
                    list.add(new VideoEntry("32 Minute Home Cardio Workout with No Equipment", "qWy_aOlB45Y"));
                    list.add(new VideoEntry("At Home Cardio Workout with No Equipment - Fat Burning Cardio Intervals", "2rzhohF5w3c"));
                    list.add(new VideoEntry("15 Minute HIIT Workout - No Equipment HIIT Cardio At Home", "0DSrudz6IVY"));
                    list.add(new VideoEntry("Quick Sweat Cardio Workout to Lose Weight & Burn Belly Fat Fast", "X1TuhAn6C-g"));
                    list.add(new VideoEntry("At Home Cardio Workout to Burn Fat and Tone (High & Low Impact Modifications)", "llRzBwD_TJw"));


                case Calendar.TUESDAY:
                    list.add(new VideoEntry("Fat Burning HIIT Pilates Workout - 35 Minute Pilates and HIIT Cardio Blend", "G9TSbIrp8qQ"));
                    list.add(new VideoEntry("Pilates Butt and Thigh Workout - 20 Minute Pilates Workout", "RZgIv9tVvLE"));
                    list.add(new VideoEntry("At Home Butt and Thigh Workout - Strength, Pilates and Barre Workout (Bored Easily)", "BtBQm4BBsR0"));
                    list.add(new VideoEntry("10 Minute Abs Workout: At Home Pilates Abs Workout for a Healthy Back", "0CQ7riVMNJc"));
                    list.add(new VideoEntry("Bikini Body Pilates - 27 Minute Abs, Butt and Thighs Pilates Workout", "OjeveoQgp6I"));
                    list.add(new VideoEntry("Relaxing Stretching Workout for Flexibility and Stress Relief - Full Body Yoga Pilates Blend", "7h_Pn7NyJ0k"));
                    list.add(new VideoEntry("Pilates Lower Body Workout to Tighten & Tone the Butt and Thighs - Home Pilates Workout Video", "9rbEqYCkXgo"));


                case Calendar.WEDNESDAY:
                    list.add(new VideoEntry("Tank Top Arms Workout - Shoulders, Arms & Upper Back Workout", "oUychjqfO8I"));
                    list.add(new VideoEntry("Intense No Equipment Upper Body Workout - At Home Upper Body Strength Without Weights", "sYYjx_W7rUY"));
                    list.add(new VideoEntry("At Home Abs and Upper Body Workout - Bodyweight Only Upper Body and Core Workout", "0xnK-XZy2Zw"));
                    list.add(new VideoEntry("At Home Upper Body Workout for Toned Arms, Shoulder & Upper Back", "cVj0SfkHjoY"));
                    list.add(new VideoEntry("Best At Home Upper Body Strength Workout for Arms, Shoulders, Chest & Back", "Z_5DW7K9QCk"));
                    list.add(new VideoEntry("Shoulders, Back, Chest and Arm Workout for Strong Toned Upper Body", "EkeWuJE3aW8"));
                    list.add(new VideoEntry("Get Strong! Upper Body Workout for Arms, Shoulders, Chest & Back (Descending Reps)", "wpTVnDwTd_M"));

                case Calendar.THURSDAY:
                    list.add(new VideoEntry("Low Impact Cardio and Toning Workout for Beginners", "GjAhM651ZPU"));
                    list.add(new VideoEntry("Fat Burning Low Impact Cardio Workout at Home", "waPCCrftvQA"));
                    list.add(new VideoEntry("32 Minute Home Cardio Workout with No Equipment", "qWy_aOlB45Y"));
                    list.add(new VideoEntry("At Home Cardio Workout with No Equipment - Fat Burning Cardio Intervals", "2rzhohF5w3c"));
                    list.add(new VideoEntry("15 Minute HIIT Workout - No Equipment HIIT Cardio At Home", "0DSrudz6IVY"));
                    list.add(new VideoEntry("Quick Sweat Cardio Workout to Lose Weight & Burn Belly Fat Fast", "X1TuhAn6C-g"));
                    list.add(new VideoEntry("At Home Cardio Workout to Burn Fat and Tone (High & Low Impact Modifications)", "llRzBwD_TJw"));

                case Calendar.FRIDAY:
                    list.add(new VideoEntry("No Equipment Butt and Thigh Workout at Home - Bodyweight Lower Body Workout", "Hp39axsuFs8"));
                    list.add(new VideoEntry("10 Minute Butt and Thigh Workout At Home - No Equipment Butt and Thigh Toning Workout", "C8X96ItgyOg"));
                    list.add(new VideoEntry("5 Minute Butt and Thigh Workout for a Bigger Butt - Exercises to Lift and Tone Your Butt and Thighs", "afghBre8NlI"));
                    list.add(new VideoEntry("Pilates Butt and Thigh Workout - 20 Minute Pilates Workout", "RZgIv9tVvLE"));
                    list.add(new VideoEntry("At Home Butt and Thigh Workout - Strength, Pilates and Barre Workout (Bored Easily)", "BtBQm4BBsR0"));
                    list.add(new VideoEntry("Brutal Butt & Thigh Workout - 30 Minute Lower Body Sculpting - Drop it Like a Squat!", "11JXbBBgfWg"));
                    list.add(new VideoEntry("10 Minute Butt and Thigh Workouts at Home - Express Glute & Thigh Toning Routine", "X663kXIufZI"));

                case Calendar.SATURDAY:
                    list.add(new VideoEntry("Fat Burning HIIT Pilates Workout - 35 Minute Pilates and HIIT Cardio Blend", "G9TSbIrp8qQ"));
                    list.add(new VideoEntry("Pilates Butt and Thigh Workout - 20 Minute Pilates Workout", "RZgIv9tVvLE"));
                    list.add(new VideoEntry("At Home Butt and Thigh Workout - Strength, Pilates and Barre Workout (Bored Easily)", "BtBQm4BBsR0"));
                    list.add(new VideoEntry("10 Minute Abs Workout: At Home Pilates Abs Workout for a Healthy Back", "0CQ7riVMNJc"));
                    list.add(new VideoEntry("Bikini Body Pilates - 27 Minute Abs, Butt and Thighs Pilates Workout", "OjeveoQgp6I"));
                    list.add(new VideoEntry("Relaxing Stretching Workout for Flexibility and Stress Relief - Full Body Yoga Pilates Blend", "7h_Pn7NyJ0k"));
                    list.add(new VideoEntry("Pilates Lower Body Workout to Tighten & Tone the Butt and Thighs - Home Pilates Workout Video", "9rbEqYCkXgo"));

            }

//            list.add(new VideoEntry("Low Impact Cardio and Toning Workout for Beginners", "GjAhM651ZPU"));
//            list.add(new VideoEntry("Android Studio Facebook", "xgMlXQ5Hk3Y"));
//            list.add(new VideoEntry("Navigation Drawer", "63Ipzp9U_bU"));
//            list.add(new VideoEntry("Recycler and card view", "A2_6mI7drVQ"));
//            list.add(new VideoEntry("Github Desktop", "om42hY4A5Qg"));
//            list.add(new VideoEntry("Collapsing Toolbar", "XMh3nbkd-hg"));
//            list.add(new VideoEntry("Googleplus Login", "ggXizPricXU"));
            VIDEO_LIST = Collections.unmodifiableList(list);
        }

        private PageAdapter adapter;
        private View videoBox;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            adapter = new PageAdapter(getActivity(), VIDEO_LIST);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            videoBox = getActivity().findViewById(R.id.video_box);
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            setListAdapter(adapter);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            String videoId = VIDEO_LIST.get(position).videoId;

            VideoFragment videoFragment =
                    (VideoFragment) getFragmentManager().findFragmentById(R.id.video_fragment_container);
            videoFragment.setVideoId(videoId);

            // The videoBox is INVISIBLE if no video was previously selected, so we need to show it now.
            if (videoBox.getVisibility() != View.VISIBLE) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // Initially translate off the screen so that it can be animated in from below.
                    videoBox.setTranslationY(videoBox.getHeight());
                }
                videoBox.setVisibility(View.VISIBLE);
            }

            // If the fragment is off the screen, we animate it in.
            if (videoBox.getTranslationY() > 0) {
                videoBox.animate().translationY(0).setDuration(ANIMATION_DURATION_MILLIS);
            }
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();

            adapter.releaseLoaders();
        }

        public void setLabelVisibility(boolean visible) {
            adapter.setLabelVisibility(visible);
        }

    }

    /**
     * Adapter for the video list. Manages a set of YouTubeThumbnailViews, including initializing each
     * of them only once and keeping track of the loader of each one. When the ListFragment gets
     * destroyed it releases all the loaders.
     */
    private static final class PageAdapter extends BaseAdapter {

        private final List<VideoEntry> entries;
        private final List<View> entryViews;
        private final Map<YouTubeThumbnailView, YouTubeThumbnailLoader> thumbnailViewToLoaderMap;
        private final LayoutInflater inflater;
        private final ThumbnailListener thumbnailListener;

        private boolean labelsVisible;

        public PageAdapter(Context context, List<VideoEntry> entries) {
            this.entries = entries;

            entryViews = new ArrayList<View>();
            thumbnailViewToLoaderMap = new HashMap<YouTubeThumbnailView, YouTubeThumbnailLoader>();
            inflater = LayoutInflater.from(context);
            thumbnailListener = new ThumbnailListener();

            labelsVisible = true;
        }

        public void releaseLoaders() {
            for (YouTubeThumbnailLoader loader : thumbnailViewToLoaderMap.values()) {
                loader.release();
            }
        }

        public void setLabelVisibility(boolean visible) {
            labelsVisible = visible;
            for (View view : entryViews) {
                view.findViewById(R.id.text).setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        }

        @Override
        public int getCount() {
            return entries.size();
        }

        @Override
        public VideoEntry getItem(int position) {
            return entries.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            VideoEntry entry = entries.get(position);

            // There are three cases here
            if (view == null) {
                // 1) The view has not yet been created - we need to initialize the YouTubeThumbnailView.
                view = inflater.inflate(R.layout.video_list_item, parent, false);
                YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
                thumbnail.setTag(entry.videoId);
                thumbnail.initialize(DeveloperKey.DEVELOPER_KEY, thumbnailListener);
            } else {
                YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.thumbnail);
                YouTubeThumbnailLoader loader = thumbnailViewToLoaderMap.get(thumbnail);
                if (loader == null) {
                    // 2) The view is already created, and is currently being initialized. We store the
                    //    current videoId in the tag.
                    thumbnail.setTag(entry.videoId);
                } else {
                    // 3) The view is already created and already initialized. Simply set the right videoId
                    //    on the loader.
                    thumbnail.setImageResource(R.drawable.loading_thumbnail);
                    loader.setVideo(entry.videoId);
                }
            }
            TextView label = ((TextView) view.findViewById(R.id.text));
            label.setText(entry.text);
            label.setVisibility(labelsVisible ? View.VISIBLE : View.GONE);
            return view;
        }

        private final class ThumbnailListener implements
                YouTubeThumbnailView.OnInitializedListener,
                YouTubeThumbnailLoader.OnThumbnailLoadedListener {

            @Override
            public void onInitializationSuccess(
                    YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
                loader.setOnThumbnailLoadedListener(this);
                thumbnailViewToLoaderMap.put(view, loader);
                view.setImageResource(R.drawable.loading_thumbnail);
                String videoId = (String) view.getTag();
                loader.setVideo(videoId);
            }

            @Override
            public void onInitializationFailure(
                    YouTubeThumbnailView view, YouTubeInitializationResult loader) {
                view.setImageResource(R.drawable.no_thumbnail);
            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView view, ErrorReason errorReason) {
                view.setImageResource(R.drawable.no_thumbnail);
            }
        }

    }

    public static final class VideoFragment extends YouTubePlayerFragment
            implements OnInitializedListener {

        private YouTubePlayer player;
        private String videoId;

        public static VideoFragment newInstance() {
            return new VideoFragment();
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            initialize(DeveloperKey.DEVELOPER_KEY, this);
        }

        @Override
        public void onDestroy() {
            if (player != null) {
                player.release();
            }
            super.onDestroy();
        }

        public void setVideoId(String videoId) {
            if (videoId != null && !videoId.equals(this.videoId)) {
                this.videoId = videoId;
                if (player != null) {
                    player.cueVideo(videoId);
                }
            }
        }

        public void pause() {
            if (player != null) {
                player.pause();
            }
        }

        @Override
        public void onInitializationSuccess(Provider provider, YouTubePlayer player, boolean restored) {
            this.player = player;
            player.addFullscreenControlFlag(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
            player.setOnFullscreenListener((VideoListDemoActivity) getActivity());
            if (!restored && videoId != null) {
                player.cueVideo(videoId);
            }
        }

        @Override
        public void onInitializationFailure(Provider provider, YouTubeInitializationResult result) {
            this.player = null;
        }

    }

    private static final class VideoEntry {
        private final String text;
        private final String videoId;

        public VideoEntry(String text, String videoId) {
            this.text = text;
            this.videoId = videoId;
        }
    }

    // Utility methods for layouting.

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density + 0.5f);
    }

    private static void setLayoutSize(View view, int width, int height) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private static void setLayoutSizeAndGravity(View view, int width, int height, int gravity) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
        params.width = width;
        params.height = height;
        params.gravity = gravity;
        view.setLayoutParams(params);
    }

}
