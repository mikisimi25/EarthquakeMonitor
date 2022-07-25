package com.example.earthquakemonitor.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.earthquakemonitor.api.RequestStatus;
import com.example.earthquakemonitor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set-up data binding
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Import view model
        MainViewModel viewModel = new ViewModelProvider(this, new MainViewModelFactory(getApplication())).get(MainViewModel.class);

        binding.eqRecycler.setLayoutManager(new LinearLayoutManager(this));

        EqAdapter adapter = new EqAdapter(this);
        adapter.setOnItemClickListener(earthquake -> Toast.makeText(MainActivity.this, earthquake.getPlace(), Toast.LENGTH_SHORT).show());

        binding.eqRecycler.setAdapter(adapter);

        //View model observer
        viewModel.getEqList().observe(this, eqList -> {
            adapter.submitList(eqList);

            if( eqList.isEmpty() ) {
                binding.emptyView.setVisibility(View.VISIBLE);
            } else {
                binding.emptyView.setVisibility(View.GONE);
            }
        });

        viewModel.getStatusMutableLiveData().observe(this, statusWithDescription -> {
            if( statusWithDescription.getStatus() == RequestStatus.LOADING ) {
                binding.loadingWheel.setVisibility(View.VISIBLE);
            } else {
                binding.loadingWheel.setVisibility(View.GONE);
            }

            if( statusWithDescription.getStatus() == RequestStatus.ERROR ) {
                Toast.makeText(this,statusWithDescription.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.downloadEarthquakes();
    }

}