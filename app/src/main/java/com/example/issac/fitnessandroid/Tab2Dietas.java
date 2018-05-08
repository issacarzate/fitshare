package com.example.issac.fitnessandroid;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ai.api.AIDataService;
import ai.api.AIListener;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;


/**
 * Created by issac on 03/02/18.
 */

public class Tab2Dietas extends Fragment implements AIListener {

    private AIService aiService;
    private AIDataService aiDataService;

    private EditText mensaje;
    private TextView conversacion;
    private Button enviar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final AIConfiguration config = new AIConfiguration("7c114f15bc1e48fabc16c398a69e898b",
                AIConfiguration.SupportedLanguages.Spanish,
                AIConfiguration.RecognitionEngine.System);


        aiDataService = new AIDataService(config);
        aiService = AIService.getService(getActivity(), config);
        aiService.setListener(this);

        View rootView = inflater.inflate(R.layout.tab2, container, false);

        enviar = (Button)rootView.findViewById(R.id.send_button);
        mensaje = (EditText)rootView.findViewById(R.id.message_box);
        conversacion = (TextView)rootView.findViewById(R.id.textView_conversation);

        mensaje.setImeActionLabel("Enviar", KeyEvent.KEYCODE_ENTER);

        mensaje.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    String strSend = mensaje.getText().toString();
                    if(TextUtils.isEmpty(strSend)){
                        mensaje.setError("Escribeno algo");
                    }else {
                        new SendRequestTask(aiDataService).execute(mensaje.getText().toString());
                    }
                    mensaje.setText("");
                    return true;
                }
                return false;
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strSend = mensaje.getText().toString();
                if(TextUtils.isEmpty(strSend)){
                    mensaje.setError("Escribeno algo");
                }else {
                    new SendRequestTask(aiDataService).execute(mensaje.getText().toString());
                }
            }
        });


        return rootView;
    }

    public class SendRequestTask extends AsyncTask<String, String, AIResponse> {
        private AIDataService aiDataService;

        public SendRequestTask(AIDataService aiDataService){
            this.aiDataService = aiDataService;
        }

        @Override
        protected AIResponse doInBackground(String... strings) {
            AIRequest aiRequest = new AIRequest();
            AIResponse aiResponse = null;

            try {
                aiRequest.setQuery(strings[0]);
                aiResponse = aiDataService.request(aiRequest);
            } catch (AIServiceException e) {
                e.printStackTrace();
            }
            return aiResponse;
        }

        @Override
        protected void onPostExecute(AIResponse aiResponse) {
            super.onPostExecute(aiResponse);
            Result result = aiResponse.getResult();
            conversacion.setText("You: " + result.getResolvedQuery() + "\r\n");
            conversacion.setText("Bot: " + result.getFulfillment().getSpeech() + "\r\n");
        }
    }

    @Override
    public void onResult(AIResponse result) {

    }

    @Override
    public void onError(AIError error) {

    }

    @Override
    public void onAudioLevel(float level) {

    }

    @Override
    public void onListeningStarted() {

    }

    @Override
    public void onListeningCanceled() {

    }

    @Override
    public void onListeningFinished() {

    }
}
