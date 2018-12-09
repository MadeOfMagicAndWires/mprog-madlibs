package online.madeofmagicandwires.joostbremmer_pset2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class StoryActivity extends AppCompatActivity {



    private Story story;
    private Text receivedText;
    private StoryInputFieldsAdapter adapter;
    /**
     * onCreate method, is called when the activity is drawn
     * @param savedInstanceState bundle of saved data from previous state
     * @see AppCompatActivity#onCreate(Bundle)
     * @see AppCompatActivity#onSaveInstanceState(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        // initialize all private members
        initElements();

        Log.d("StoryActivity", "Amount of placeholders: " + story.getPlaceholderCount());

        Log.d("StoryActivity", "The following placeholders were found:");
        for(int i=0;i<story.getPlaceholderCount();i++){
            Log.d(Integer.toString(i), story.getPlaceholder(i).toString());
        }
    }

    /**
     * Initializes all private elements if they are not set already.
     * @see #setTxt()
     * @see #setStory(Text)
     */
    public void initElements() {
        if(receivedText == null || story == null) {
            try {
                setTxt();
            } catch (NullPointerException e) {
                Log.e("Could not initialise Text", e.getMessage());
                finish();
            }
            setStory(receivedText);
            setRecyclerView();
        }
        if(adapter != null) {
            setSubmitButton();
        }
    }

    /**
     * Retrieves the Text object from the previous activity
     */
    private void setTxt() throws NullPointerException {
        Intent intent = getIntent();
        Text txt = (Text) intent.getSerializableExtra(MainActivity.TEXT_EXTRA_NAME);
        if(txt != null) {
            receivedText = txt;
        } else {
            throw new NullPointerException("No text received from previous activity");
        }
    }

    /**
     * Initializes the Story instance from Text object
     */
    private void setStory(Text txt) {
        story = TextReader.getStoryFromText(getApplicationContext(), txt);

    }

    /**
     * Initializes the RecyclerView with the right adapter and layoutmanagers
     */
    private void setRecyclerView() {
        RecyclerView container = findViewById(R.id.inputContainer);
        if(container != null) {
            adapter = new StoryInputFieldsAdapter(this, story);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
            container.setAdapter(adapter);
            container.setLayoutManager(manager);
        }

    }

    /**
     * Initiates the final "Submit" so that when clicked it applies the replacement changes and shows
     * the Story's text.
     */
    private void setSubmitButton() {
        Button btn = findViewById(R.id.inputCommit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean changesCommited = adapter.commitReplacements();
                if(changesCommited) {
                    showStory();
                }
            }
        });
    }

    /**
     * Shows the Story's text in the current activity
     */
    private void showStory() {
        setContentView(R.layout.show_story);
        TextView storyText = findViewById(R.id.storyText);
        storyText.setText(story.toString());
    }




}