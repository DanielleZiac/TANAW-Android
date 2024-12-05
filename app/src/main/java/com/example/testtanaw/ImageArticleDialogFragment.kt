package com.example.testtanaw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class ImageArticleDialogFragment : DialogFragment() {

    private var imageResource: Int = 0  // Default value
    private var articleText: String = ""  // Default value

    companion object {
        const val IMAGE_KEY = "image_resource"
        const val ARTICLE_KEY = "article_text"

        fun newInstance(imageResource: Int, articleText: String): ImageArticleDialogFragment {
            val fragment = ImageArticleDialogFragment()
            val bundle = Bundle().apply {
                putInt(IMAGE_KEY, imageResource)
                putString(ARTICLE_KEY, articleText)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_image_article, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageResource = arguments?.getInt(IMAGE_KEY) ?: 0
        articleText = arguments?.getString(ARTICLE_KEY) ?: ""

        val imageView: ImageView = view.findViewById(R.id.dialog_image)
        val textView: TextView = view.findViewById(R.id.dialog_article)

        imageView.setImageResource(imageResource)
        textView.text = articleText
    }
}
