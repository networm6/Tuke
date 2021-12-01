package simon.tuke

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import simon.tuke.databinding.ActivityMainBinding
import java.io.Serializable

class MainActivity : AppCompatActivity() ,Serializable{
    private var name: String by tuke("默认值",useMemory = true)

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Tuke.tukeGet()
        Tuke.init(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.sampleText.setText(name)
        Tuke(null).

    }

    override fun onDestroy() {
        super.onDestroy()
        name= binding.sampleText.text.toString()
    }
}