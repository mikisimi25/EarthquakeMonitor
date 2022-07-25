package com.example.earthquakemonitor.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.earthquakemonitor.Earthquake;
import com.example.earthquakemonitor.api.RequestStatus;
import com.example.earthquakemonitor.api.StatusWithDescription;
import com.example.earthquakemonitor.database.EqDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private final MainRepository repository;
    private MutableLiveData<StatusWithDescription> statusMutableLiveData = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        //Creamos la BD
        EqDatabase database = EqDatabase.getDatabase(application);
        //Creamos el repositorio
        this.repository = new MainRepository(database);
    }

    //También se activa esto al guardar los terremotos
    public LiveData<List<Earthquake>> getEqList() {
        return repository.getEqList();
    }

    public MutableLiveData<StatusWithDescription> getStatusMutableLiveData() {
        return statusMutableLiveData;
    }

    //Trae los terremotos
    public void downloadEarthquakes() {
//        repository.getEarthquakes(earthquakeList -> {
//            eqList.setValue(earthquakeList);
//        });

        statusMutableLiveData.setValue(new StatusWithDescription(RequestStatus.LOADING, ""));
        repository.downloadAndSaveEarthquakes(new MainRepository.DownloadStatusListener() {
            @Override
            public void downloadSuccess() {
                statusMutableLiveData.setValue(new StatusWithDescription(RequestStatus.DONE, ""));
            }

            @Override
            public void downloadError(String message) {
                statusMutableLiveData.setValue(new StatusWithDescription(RequestStatus.ERROR, message));
            }
        });
    }
}
