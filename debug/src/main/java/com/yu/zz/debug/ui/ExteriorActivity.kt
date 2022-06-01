package com.yu.zz.debug.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yu.zz.debug.R
import com.yu.zz.debug.databinding.ActivityExteriorBinding

class ExteriorActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityExteriorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityExteriorBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mBinding.rv.adapter = ScrollAdapter()
        mBinding.rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                Log.e("Rain", dx.toString())
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }
}

class ScrollAdapter : RecyclerView.Adapter<ScrollViewHolder>() {
    private val mList = mutableListOf<String>()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrollViewHolder {
        return ScrollViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return 100
    }

    override fun onBindViewHolder(holder: ScrollViewHolder, position: Int) {
        Log.e("rain", position.toString())
        holder.bind(position.toString())
    }

}


class ScrollViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mTv: TextView = itemView as TextView

    constructor(parent: ViewGroup) : this(
        LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
    )

    public fun bind(content: String) {
        mTv.text = content
    }
}

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_main, container, false)
        return mView
    }
}