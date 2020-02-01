package com.dainc.kotlintraining.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.dainc.kotlintraining.R
import com.dainc.kotlintraining.model.ShuukeiModel
import java.util.*

class ListShuukeiAdapter(private var activity: Activity, var shuukeiList: ArrayList<ShuukeiModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return shuukeiList.size
    }

    override fun getItem(position: Int): Any {
        return shuukeiList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    private inner class ViewHolder {
        internal var mRoleName: TextView? = null
        internal var mMale: TextView? = null
        internal var mFemale: TextView? = null
        internal var mAgeMax19: TextView? = null
        internal var mAgeMin20: TextView? = null
        internal var mNotFull: TextView? = null
        internal var mNotAge: TextView? = null

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ViewHolder
        val inflater = activity.layoutInflater

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_shuukei, null)
            holder = ViewHolder()
            holder.mRoleName = convertView!!.findViewById(R.id.roleName) as TextView
            holder.mMale = convertView.findViewById(R.id.male) as TextView
            holder.mFemale = convertView.findViewById(R.id.female) as TextView
            holder.mAgeMax19 = convertView.findViewById(R.id.ageMax19) as TextView
            holder.mAgeMin20 = convertView.findViewById(R.id.ageMin20) as TextView
            holder.mNotFull = convertView.findViewById(R.id.notFull) as TextView
            holder.mNotAge = convertView.findViewById(R.id.notAge) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val item = shuukeiList[position]
        holder.mRoleName!!.text = item.getRoleName()
        holder.mMale!!.text = item.getMale().toString()
        holder.mFemale!!.text = item.getFemale().toString()
        holder.mAgeMax19!!.text = item.getAgeMax19().toString()
        holder.mAgeMin20!!.text = item.getAgeMin20().toString()
        holder.mNotFull!!.text = item.getNotFull().toString()
        holder.mNotAge!!.text = item.getNotAge().toString()
        return convertView
    }
}
