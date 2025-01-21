package ro.pub.cs.systems.eim.practicaltest02v10;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PracticalTest02MainActivityv10 extends AppCompatActivity {

    private EditText pokemonNameInput;
    private TextView pokemonTypes;
    private TextView pokemonAbilities;
    private ImageView pokemonImage;
    private Button fetchPokemonButton;
    private Button navigateButton;
    private Button navigateToFcmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02v10_main);

        // Inițializăm elementele UI
        pokemonNameInput = findViewById(R.id.pokemon_name_input);
        pokemonTypes = findViewById(R.id.pokemon_types);
        pokemonAbilities = findViewById(R.id.pokemon_abilities);
        pokemonImage = findViewById(R.id.pokemon_image);
        fetchPokemonButton = findViewById(R.id.fetch_pokemon_button);
        navigateButton = findViewById(R.id.navigate_to_activity5);

        // Inițializăm butonul pentru navigarea la activitatea FCM
        navigateToFcmButton = findViewById(R.id.navigate_to_activity5);

        // Setăm listener pentru butonul de obținere a datelor
        fetchPokemonButton.setOnClickListener(v -> fetchPokemonData());

        // Setăm listener pentru navigarea la activitatea FCM
        navigateToFcmButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, FCMTopicActivity.class);
            startActivity(intent);
        });
    }

    private void fetchPokemonData() {
        String pokemonName = pokemonNameInput.getText().toString().trim();
        if (pokemonName.isEmpty()) {
            Toast.makeText(this, "Please enter a Pokemon name", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                // URL-ul serviciului web
                String apiUrl = "https://pokeapi.co/api/v2/pokemon/" + pokemonName.toLowerCase();
                HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
                connection.setRequestMethod("GET");

                // Verificăm codul de răspuns
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Logăm răspunsul pentru debugging
                    Log.d("PokemonResponse", response.toString());

                    // Trimitem răspunsul spre parsare
                    parsePokemonData(response.toString());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Failed to fetch data: " + responseCode, Toast.LENGTH_SHORT).show());
                }
                connection.disconnect();
            } catch (Exception e) {
                Log.e("PokemonFetchError", "Error fetching data", e);
                runOnUiThread(() -> Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void parsePokemonData(String data) {
        try {
            // Parsăm JSON-ul
            JSONObject jsonObject = new JSONObject(data);

            // Tipurile Pokémon-ului
            JSONArray typesArray = jsonObject.getJSONArray("types");
            StringBuilder typesBuilder = new StringBuilder();
            for (int i = 0; i < typesArray.length(); i++) {
                JSONObject type = typesArray.getJSONObject(i).getJSONObject("type");
                typesBuilder.append(type.getString("name")).append(", ");
            }
            String types = typesBuilder.toString().replaceAll(", $", "");

            // Abilitățile Pokémon-ului
            JSONArray abilitiesArray = jsonObject.getJSONArray("abilities");
            StringBuilder abilitiesBuilder = new StringBuilder();
            for (int i = 0; i < abilitiesArray.length(); i++) {
                JSONObject ability = abilitiesArray.getJSONObject(i).getJSONObject("ability");
                abilitiesBuilder.append(ability.getString("name")).append(", ");
            }
            String abilities = abilitiesBuilder.toString().replaceAll(", $", "");

            // URL-ul imaginii Pokémon-ului
            String imageUrl = jsonObject.getJSONObject("sprites").getString("front_default");

            // Actualizăm interfața grafică
            updateGUI(types, abilities, imageUrl);
        } catch (JSONException e) {
            Log.e("ParseError", "Error parsing JSON", e);
            runOnUiThread(() -> Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show());
        }
    }

    private void updateGUI(String types, String abilities, String imageUrl) {
        runOnUiThread(() -> {
            // Afișăm tipurile și abilitățile în TextView-uri
            pokemonTypes.setText("Types: " + types);
            pokemonAbilities.setText("Abilities: " + abilities);

            // Afișăm imaginea folosind metoda custom fără Glide
            loadImageFromUrl(imageUrl);
        });
    }

    private void loadImageFromUrl(String url) {
        new Thread(() -> {
            try {
                // Descarcă imaginea din URL
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);

                // Actualizează imaginea în UI
                runOnUiThread(() -> pokemonImage.setImageBitmap(bitmap));
            } catch (Exception e) {
                Log.e("ImageLoadError", "Error loading image", e);
            }
        }).start();
    }
}
