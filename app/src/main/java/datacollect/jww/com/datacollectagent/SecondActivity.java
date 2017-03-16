package datacollect.jww.com.datacollectagent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import datacollect.jww.com.datacollectagent.adapter.SecondListAdapter;

public class SecondActivity extends AppCompatActivity {

    private RecyclerView rv;
    private SecondListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        mAdapter = new SecondListAdapter();
        this.rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);
    }
}
