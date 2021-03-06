package caffeinatedandroid.rpibotcontrol.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import caffeinatedandroid.rpibotcontrol.R

class HomeFragment : Fragment() {

    // HomeViewModel is shared with Activity to enable updates from UI interactions, e.g. onClicks
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.textConnectionStatus.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}