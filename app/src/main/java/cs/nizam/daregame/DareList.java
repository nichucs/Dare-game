package cs.nizam.daregame;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class DareList extends AppCompatActivity {

    ListView listView;
    Button add;
    private ActionsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dare_list);
        if (getActionBar() != null) getActionBar().setDisplayHomeAsUpEnabled(true);
        add = (Button) findViewById(R.id.new_button);

        // Here we query database
        final DatabaseHandler databaseHandler = new DatabaseHandler(this);
        final Cursor mCursor = databaseHandler.getCursor();
        listView = (ListView) findViewById(R.id.action_list);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                adapter = new ActionsListAdapter(DareList.this, mCursor, 0, R.layout.listitem);
                listView.setAdapter(adapter);
                adapter.setListView(listView);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DareList.this);
                builder.setTitle("New Dare item");

                // Set up the input
                final EditText input = new EditText(DareList.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_Text = input.getText().toString();
                        if (!TextUtils.isEmpty(m_Text)) {
                            databaseHandler.addAction(m_Text);
                            adapter.setCursor(databaseHandler.getCursor());
                            adapter.notifyDataSetChanged();
                            listView.smoothScrollToPosition(databaseHandler.getActionsCount()-1);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }

}
