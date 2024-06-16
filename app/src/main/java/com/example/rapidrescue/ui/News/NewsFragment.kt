package com.example.rapidrescue.ui.News

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidrescue.R

class NewsFragment : Fragment(), OnItemClickListener {

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView
    private lateinit var webView: WebView
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        webView = view.findViewById(R.id.webView)
        searchView = view.findViewById(R.id.searchView)
        viewModel = ViewModelProvider(this).get(NewsViewModel::class.java)

        // Configure WebView settings
        configureWebView()

        viewModel.news.observe(viewLifecycleOwner, Observer { news ->
            if (news != null) {
                newsAdapter = NewsAdapter(news, this)
                recyclerView.adapter = newsAdapter
                recyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                Log.e("NewsFragment", "News data is null")
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.searchNews(query, "5a8a09c106db4d889bb99154405e4c82")
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        viewModel.getTopHeadlines("in", "5a8a09c106db4d889bb99154405e4c82")
    }

    override fun onItemClick(news: News) {
        webView.loadUrl(news.url)
        webView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun configureWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.webViewClient = WebViewClient()
    }

    // Handle back press for WebView navigation
    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                webView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                isEnabled = false
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}





