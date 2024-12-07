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

    companion object {
        private const val IMAGE_KEY = "image_resource"
        private const val ARTICLE_KEY = "article_text"
    }
    
    private var imageResource: Int = 0  // Default value
    private var articleText: String = ""  // Default value

    fun newInstance(imageResource: Int, articleText: String): ImageArticleDialogFragment {
        val fragment = ImageArticleDialogFragment()
        val bundle = Bundle()
        bundle.putInt(IMAGE_KEY, imageResource);
        bundle.putString(ARTICLE_KEY, articleText);
        fragment.setArguments(bundle);
        return fragment;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
                            savedInstanceState: Bundle?) {
        return inflater.inflate(R.layout.dialog_image_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);

        val args = arguments
        if (args != null) {
            imageResource = args.getInt(IMAGE_KEY, 0)
            articleText = args.getString(ARTICLE_KEY, "")
        }

        val imageView: ImageView = view.findViewById(R.id.dialog_image)
        val textView: TextView = view.findViewById(R.id.dialog_article)

        imageView.setImageResource(imageResource);
        textView.setText(articleText);
    }
}
