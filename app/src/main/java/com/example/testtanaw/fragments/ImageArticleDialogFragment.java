package com.example.testtanaw.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.example.testtanaw.R;

public class ImageArticleDialogFragment extends DialogFragment {

    private int imageResource = 0;  // Default value
    private String articleText = "";  // Default value

    private static final String IMAGE_KEY = "image_resource";
    private static final String ARTICLE_KEY = "article_text";

    public static ImageArticleDialogFragment newInstance(int imageResource, String articleText) {
        ImageArticleDialogFragment fragment = new ImageArticleDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IMAGE_KEY, imageResource);
        bundle.putString(ARTICLE_KEY, articleText);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the dialog layout
        return inflater.inflate(R.layout.dialog_image_article, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Retrieve the arguments passed to the fragment
        if (getArguments() != null) {
            imageResource = getArguments().getInt(IMAGE_KEY, 0);
            articleText = getArguments().getString(ARTICLE_KEY, "");
        }

        // Find the views in the dialog layout
        ImageView imageView = view.findViewById(R.id.dialog_image);
        TextView textView = view.findViewById(R.id.dialog_article);

        // Set the image resource and article text
        imageView.setImageResource(imageResource);
        textView.setText(articleText);
    }
}
