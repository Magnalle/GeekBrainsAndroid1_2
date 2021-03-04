package com.magnalleexample.geekbrainsandroid1_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import com.magnalleexample.geekbrainsandroid1_2.databinding.ActivityMainBinding;
import com.magnalleexample.geekbrainsandroid1_2.generated.callback.OnClickListener;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        viewModel = (new ViewModelProvider(this, new SavedStateViewModelFactory(getApplication(), this, savedInstanceState))).get(MainActivityViewModel.class);
        viewModel.binding = binding;
        setUpActions(binding);
        viewModel.updateResult();
    }

    private void setUpActions(ActivityMainBinding binding){
        binding.setViewModel(viewModel);
        binding.buttonPlus.setOnClickListener((view)->{
            viewModel.binaryOperation(MainActivityViewModel.Operation.PLUS);
        });
        binding.buttonMinus.setOnClickListener((view)->{
            viewModel.binaryOperation(MainActivityViewModel.Operation.MINUS);
        });
        binding.buttonDIV.setOnClickListener((view)->{
            viewModel.binaryOperation(MainActivityViewModel.Operation.DIVIDE);
        });
        binding.buttonMUL.setOnClickListener((view)->{
            viewModel.binaryOperation(MainActivityViewModel.Operation.MULTIPLY);
        });
        binding.button1div.setOnClickListener((view)->{
            viewModel.unaryOperation(MainActivityViewModel.Operation.ONE_DIV);
        });
        binding.buttonSqr.setOnClickListener((view)->{
            viewModel.unaryOperation(MainActivityViewModel.Operation.SQR);
        });
        binding.buttonSqrt.setOnClickListener((view)->{
            viewModel.unaryOperation(MainActivityViewModel.Operation.SQRT);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.onSaveState();
    }
}