package com.dainc.kotlintraining.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import com.dainc.kotlintraining.R
import com.dainc.kotlintraining.model.UserModel

import java.util.ArrayList

class ListUserAdapter(private var activity: Activity, var userList: ArrayList<UserModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return userList.size
    }

    override fun getItem(position: Int): Any {
        return userList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private inner class ViewHolder {
        internal var mSNo: TextView? = null
        internal var mProduct: TextView? = null
        internal var mCategory: TextView? = null
        internal var mPrice: TextView? = null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ViewHolder
        val inflater = activity.layoutInflater

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_ichiran, null)
            holder = ViewHolder()
            holder.mSNo = convertView!!.findViewById(R.id.sNo) as TextView
            holder.mProduct = convertView.findViewById(R.id.product) as TextView
            holder.mCategory = convertView
                .findViewById(R.id.category) as TextView
            holder.mPrice = convertView.findViewById(R.id.price) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val item = userList[position]
        holder.mSNo!!.text = item.getsNo().toString()
        holder.mProduct!!.text = item.getUserId().toString()
        holder.mCategory!!.text = item.getFamilyName().toString() + " " + item.getFirstName().toString()
        if (item.getAdmin() === 1)
            holder.mPrice!!.text = "★" + item.getAuthorityName().toString()
        else
            holder.mPrice!!.text = item.getAuthorityName().toString()

        return convertView
    }
}
