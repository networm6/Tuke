package simon.tuke;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

public class TestActivity extends Activity {
    EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Tuke.init(this);
        setContentView(R.layout.test);
        editText=findViewById(R.id.ed);
        editText.setText(Tuke.tukeGet("key","s"));
        Tuke.tukeRemoveAll();
    }
    public void btn(View view){
        Tuke.tukeWrite("key",editText.getText().toString());
    }
}
