package com.example.rapidrescue.ui.News

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidrescue.R


class NewsFragment : Fragment(), OnItemClickListener {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        viewModel.news.observe(viewLifecycleOwner, Observer { news ->
            if (news != null) {
                newsAdapter = NewsAdapter(news, this)
                recyclerView.adapter = newsAdapter
                recyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                Log.e("NewsFragment", "News data is null")
            }
        })

        viewModel.getTopHeadlines("in", "5a8a09c106db4d889bb99154405e4c82")
    }

    override fun onItemClick(news: News) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(news.url))
        startActivity(intent)
    }
}



