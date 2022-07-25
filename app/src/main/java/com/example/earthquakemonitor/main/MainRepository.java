package com.example.earthquakemonitor.main;

import androidx.lifecycle.LiveData;

import com.example.earthquakemonitor.Earthquake;
import com.example.earthquakemonitor.api.EarthquakeJSONResponse;
import com.example.earthquakemonitor.api.EqApiClient;
import com.example.earthquakemonitor.api.Feature;
import com.example.earthquakemonitor.database.EqDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {

    private EqDatabase database;

    public interface DownloadStatusListener {
        void downloadSuccess();
        void downloadError(String message);
    }

    public MainRepository(EqDatabase database) {
        this.database = database;
    }

    //Al guardar los terremotos, esto se activa
    public LiveData<List<Earthquake>> getEqList() {
        return database.eqDao().getEarthquakes();
    }

    public void downloadAndSaveEarthquakes(DownloadStatusListener downloadStatusListener) {

        //obtenemos el servicio
        EqApiClient.EqService service = EqApiClient.getInstance().getService();

        //enqueue -> agrega la request( service.getEarthquakes() ) a una cola, la cual traera los datos que necesitamos;
        //nos permite hacer el request que se va el servidor en un hilo que es diferente al hilo principal
        service.getEarthquakes().enqueue(new Callback<EarthquakeJSONResponse>() {
            @Override
            public void onResponse(Call<EarthquakeJSONResponse> call, Response<EarthquakeJSONResponse> response) {
                List<Earthquake> earthquakeList = getEarthquakesWithMoshi(response.body());

                EqDatabase.databaseWriteExecutor.execute(() -> {
                    //Se trae los terremotos y los guarda en la BD
                    database.eqDao().insertAll(earthquakeList);
                });

                downloadStatusListener.downloadSuccess();
            }

            @Override
            public void onFailure(Call<EarthquakeJSONResponse> call, Throwable t) {
                downloadStatusListener.downloadError("There was an error in downloading earthquakes, check internet connection");
            }
        });
    }

    private List<Earthquake> getEarthquakesWithMoshi(EarthquakeJSONResponse body) {
        ArrayList<Earthquake> eqList = new ArrayList<>();

        List<Feature> features = body.getFeatures();

        for (Feature feature: features) {
            String id = feature.getId();
            double magnitude = feature.getProperties().getMag();
            String place = feature.getProperties().getPlace();
            long time = feature.getProperties().getTime();

            double longitude = feature.getGeometry().getLongitude();
            double latitude = feature.getGeometry().getLatitude();

            Earthquake earthquake = new Earthquake(id, place, magnitude, time, latitude, longitude);
            eqList.add(earthquake);
        }

        return eqList;
    }

}
