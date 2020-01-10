package com.coldhammer.teg.passwordkeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.coldhammer.teg.passwordkeeper.PasswordGenerator.CharacterList;
import com.coldhammer.teg.passwordkeeper.PasswordGenerator.PasswordGenerator;
import com.coldhammer.teg.passwordkeeper.Serializer.GObjectSerializer;

import java.security.SecureRandom;
import java.util.HashMap;

public class ItemPage extends AppCompatActivity {

    HashMap<String, String> keySet;
    boolean onlyEdit;
    boolean editMode = false;
    boolean isShown = false;

    EditText etService;
    EditText etUsername;
    EditText etName;
    EditText etLastname;
    EditText etPassword;
    EditText etEmail;

    TextView txService;

    Toolbar toolbar;

    Button showPasswordButton;
    Button generatePassword;
    Button cancel;
    Button saveButton;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Cancel();
    }

    CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_page);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        collapsingToolbarLayout = findViewById(R.id.collapsingTooldBar);
        collapsingToolbarLayout.setCollapsedTitleTextColor(0x77ffffff);
        collapsingToolbarLayout.setExpandedTitleColor(0xffffffff);

        txService = findViewById(R.id.txt_service);
        etService = findViewById(R.id.et_service);
        etUsername = findViewById(R.id.et_username);
        etName = findViewById(R.id.et_name);
        etLastname = findViewById(R.id.et_lastname);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);

        showPasswordButton = findViewById(R.id.show_password);
        generatePassword = findViewById(R.id.generate_password);
        cancel = findViewById(R.id.cancel);
        saveButton = findViewById(R.id.save);

        final Intent intent = getIntent();
        onlyEdit = intent.getBooleanExtra("onlyEdit", false);

        if(!onlyEdit) {
            keySet = GObjectSerializer.createHashMap(intent.getStringExtra("data"));
            collapsingToolbarLayout.setTitle( keySet.get("service"));
            etService.setText(keySet.get("service"));
            etUsername.setText(keySet.get( "username"));
            etName.setText(keySet.get( "name"));
            etLastname.setText(keySet.get( "lastname"));
            etPassword.setText(keySet.get("password"));
            etEmail.setText(keySet.get("email"));
        }

        edit(onlyEdit);

        showPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShown) {
                    // show password
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPasswordButton.setText("Show Password");
                } else {
                    // hide password
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPasswordButton.setText("Hide Password");
                }
                isShown=!isShown;
                generatePassword.setEnabled(isShown);
            }
        });
        generatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharacterList lowerList = new CharacterList("abcdefghijklmnopqrstuvwxyz".toCharArray(), -1);
                CharacterList upperList = new CharacterList("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray(), -1);
                CharacterList numberList = new CharacterList("0123456789".toCharArray(), -1);
                CharacterList symbolList = new CharacterList("!\"#$%^&*()';:,<>./?\\|`~@*-_=+{}[]".toCharArray(), -1);
                etPassword.setText(String.copyValueOf(PasswordGenerator.generatePassword(12, new int[] {1,0,0,0,0,0,0,0,0,0,0,0}, new SecureRandom(), lowerList, upperList, numberList, symbolList)));
            }
        });

        etEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                saveButton.callOnClick();
                return false;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == false)
                {
                    edit(true);

                }else{
                    Intent returnIntent = new Intent();
                    keySet = new HashMap<>();
                    keySet.put("service", etService.getText().toString());
                    keySet.put("username", etUsername.getText().toString());
                    keySet.put("name", etName.getText().toString());
                    keySet.put("lastname", etLastname.getText().toString());
                    keySet.put("password", etPassword.getText().toString());
                    keySet.put("email", etEmail.getText().toString());
                    returnIntent.putExtra("data", GObjectSerializer.keySetToStringBuffer(keySet, null).toString());
                    returnIntent.putExtra("position", intent.getIntExtra("position", -1));
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cancel();
            }
        });
    }

    public void Cancel()
    {
        if(editMode==false || onlyEdit == true)
        {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED,returnIntent);
            finish();
        }else{
            edit(onlyEdit);
        }

    }

    public void edit(boolean check)
    {
        editMode = check;
        if(editMode)
        {
            collapsingToolbarLayout.setTitle("Edit Mode");
        }else{
            collapsingToolbarLayout.setTitle(etService.getText());
        }
        etUsername.setEnabled(editMode);
        etPassword.setEnabled(editMode);
        etName.setEnabled(editMode);
        etLastname.setEnabled(editMode);
        etEmail.setEnabled(editMode);
        etService.setVisibility(getVisibility(check));
        txService.setVisibility(getVisibility(check));
        txService.setEnabled(check);
        setNames();
    }

    public int getVisibility(boolean isVisible)
    {
        if(isVisible) return 0;
        return 8;
    }

    public void setNames()
    {
        if(onlyEdit)
        {
            saveButton.setText("Save");
            cancel.setText("Cancel");
            return;
        }
        if(editMode)
        {
            saveButton.setText("Save");
            cancel.setText("Back");
        }else{
            saveButton.setText("Modify");
            cancel.setText("Cancel");
        }
    }

}
