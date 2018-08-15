package tt.reddit.application.com.myapplicationreddt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login_button;
    private ProgressBar login_ProgressB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_button = (Button)findViewById(R.id.btn_login);
        login_ProgressB = (ProgressBar)findViewById(R.id.login_progressbar);
        username = (EditText)findViewById(R.id.input_username);
        password = (EditText)findViewById(R.id.input_password);
        login_ProgressB.setVisibility(View.GONE);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usernameC = username.getText().toString();
                String passwordC = password.getText().toString();

                if(!usernameC.equals("") && !passwordC.equals("")){

                    login_ProgressB.setVisibility(View.VISIBLE);

                }

            }
        });
    }
}
