package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.testtanaw.R;

public class ImageArticleDialogFragment extends DialogFragment {

    private static final String IMAGE_KEY = "image_resource";
    private static final String ARTICLE_KEY = "article_text";
    
    private int imageResource = 0;  // Default value
    private String articleText = "";  // Default value

    public static ImageArticleDialogFragment newInstance(int imageResource, String articleText) {
        ImageArticleDialogFragment fragment = new ImageArticleDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IMAGE_KEY, imageResource);
        bundle.putString(ARTICLE_KEY, articleText);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                           @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_image_article, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            imageResource = args.getInt(IMAGE_KEY, 0);
            articleText = args.getString(ARTICLE_KEY, "");
        }

        ImageView imageView = view.findViewById(R.id.dialog_image);
        TextView textView = view.findViewById(R.id.dialog_article);

        imageView.setImageResource(imageResource);
        textView.setText(articleText);
    }
}
