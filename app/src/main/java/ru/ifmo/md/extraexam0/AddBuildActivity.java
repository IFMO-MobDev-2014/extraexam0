package ru.ifmo.md.extraexam0;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class AddBuildActivity extends ActionBarActivity implements View.OnClickListener {

    Button btnAdd;
    EditText edtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_build);
        btnAdd = ((Button) findViewById(R.id.btnAdd));
        btnAdd.setOnClickListener(this);
        edtName = ((EditText) findViewById(R.id.edtName));
    }

    public static final String EXTRA_NAME = "name";

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnAdd) {
            Intent result = new Intent();
            result.putExtra(EXTRA_NAME, edtName.getText().toString());
            setResult(RESULT_OK, result);
            finish();
        }
    }
}
