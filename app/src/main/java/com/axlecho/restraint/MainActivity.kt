package com.axlecho.restraint

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.axlecho.restraint.data.Filter
import com.axlecho.restraint.data.Info
import com.axlecho.restraint.ui.DragAndDrapHelper
import com.axlecho.restraint.ui.ListViewAdapter
import com.axlecho.restraint.ui.OnDataEditListener
import com.axlecho.restraint.ui.OnItemLongClickListener
import com.axlecho.restraint.utils.TimeUtils
import com.axlecho.restraint.utils.UsageStatsUtils


class MainActivity : AppCompatActivity(), OnDataEditListener<Info>, OnItemLongClickListener {


    private var phoneUsageData = mutableListOf<Info>()
    private var cigaretteData = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (UsageStatsUtils.checkPermission(this)) {
            val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
            startActivity(intent)
        }

        cigaretteData = loadCigarette()
    }

    override fun onResume() {
        super.onResume()
        if (UsageStatsUtils.checkPermission(this)) {
            findViewById<View>(R.id.list).visibility = View.GONE
            findViewById<View>(R.id.error_tip).visibility = View.VISIBLE
            findViewById<View>(R.id.error_tip).setOnClickListener {
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                startActivity(intent)
            }
        } else {
            val view = findViewById<RecyclerView>(R.id.list)

            view.layoutManager = LinearLayoutManager(this)
            phoneUsageData =  UsageStatsUtils.getUsageInfo(this, TimeUtils.dayZero(), TimeUtils.now())
            phoneUsageData.sortByDescending { it.time }
            UsageStatsUtils.applyAllFilter(this,phoneUsageData)
            val adapter = ListViewAdapter(this,phoneUsageData)
            view.adapter = adapter
             val itemTouchHelper = ItemTouchHelper(DragAndDrapHelper(adapter, phoneUsageData,this))
            itemTouchHelper.attachToRecyclerView(view)
            adapter.listener = this
        }
        bindCigarette()
        bindPhoneUsage()
    }

    override fun onItemLongClick(view: View, position: Int) {
        val p = PopupMenu(this, view)
        // p.menuInflater.inflate(R.menu.popup_menu_example, p.menu)
        p.menu.add(Filter.GAME)
        p.menu.add(Filter.COMIC)
        p.menu.add(Filter.BROWSE)
        p.menu.add(Filter.VIDEO)
        p.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                UsageStatsUtils.saveFilter(this@MainActivity,phoneUsageData[position],item.title.toString())
                UsageStatsUtils.applyAllFilter(this@MainActivity,phoneUsageData)
                bindPhoneUsage()
                return true
            }
        })
        p.show()
    }

    override fun onItemSwiped(module: Info, pos: Int) {
        UsageStatsUtils.saveFilter(this,module, Filter.IGNORE)
        UsageStatsUtils.applyAllFilter(this,phoneUsageData)
        bindPhoneUsage()
    }

    override fun onItemMoved(module: Info, from: Int, to: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun bindPhoneUsage() {
        val filters = UsageStatsUtils.loadFilter(this)
        var phoneUsageStr = ""
        var total = 0L
        for(filter in filters) {
            if(filter.type == Filter.IGNORE) {
                continue
            }

            var time = 0L
            for (info in phoneUsageData) {
                if(filter.type == info.type) {
                    time += info.time
                }
            }
            total += time
            phoneUsageStr += String.format("%s : %s",filter.type,TimeUtils.readableTime(time))
        }
        phoneUsageStr += String.format("%s : %s","total",TimeUtils.readableTime(total))

        val view = findViewById<TextView>(R.id.phone_usage_panel)
        view.text = phoneUsageStr
    }

    private fun bindCigarette() {
        val view = findViewById<TextView>(R.id.cigarette_panel)
        view.text = cigaretteData.toString()

        view.setOnClickListener {
            cigaretteData ++
            saveCigarette(cigaretteData)
            bindCigarette()
        }
    }

    private fun loadCigarette():Int {
        val spf = getSharedPreferences("cigarette",Context.MODE_PRIVATE)
        val timeStamp = spf.getLong("timestamp",0)
        if(TimeUtils.dayZero() > timeStamp) {
            saveCigarette(0)
        }
        return spf.getInt("time",0)
    }

    private fun saveCigarette(cigarette:Int) {
        val spf = getSharedPreferences("cigarette",Context.MODE_PRIVATE)
        spf.edit().putInt("time",cigarette).apply()
        spf.edit().putLong("timestamp",TimeUtils.now()).apply()
    }
}
