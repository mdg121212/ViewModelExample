package com.mattg.viewmodelexample.viewModels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mattg.viewmodelexample.activities.MainActivity;
import com.mattg.viewmodelexample.R;
import com.mattg.viewmodelexample.databinding.ActivitySecondBinding;
import com.mattg.viewmodelexample.database.entities.Ticket;
import com.mattg.viewmodelexample.utils.AppError;
import com.mattg.viewmodelexample.utils.CurrencyTextWatcher;
import com.mattg.viewmodelexample.utils.CurrencyUtil;
import com.mattg.viewmodelexample.utils.Utils;

import java.text.DecimalFormat;

public class SecondActivity extends AppCompatActivity {

    public MainViewModel viewModel;
    public TextView orderId;
    public TextView subTotal;
    public TextView uniqueId;
    public TextView firstName;
    public TextView lastName;
    public TextView orderSent;
    public Button backButton;
    com.mattg.viewmodelexample.database.entities.Ticket currentTicket;
    private ActivitySecondBinding binding;
    private DecimalFormat df = new DecimalFormat("0.00");
    CurrencyTextWatcher tw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        currentTicket = (Ticket) getIntent().getExtras().get("ticket");
      //  viewModel.setCurrentTicket(currentTicket);

        initViews();
        observeViewModel();
        setOnClickListeners();
        backButton.setOnClickListener(view -> {
            Intent backIntent = new Intent(this, MainActivity.class);
            startActivity(backIntent);
        });


    }

    /**
     * This method handles the observation of viewmodel livedata values.  The view should not be concerned
     * with calculation of data, manipulation of data, etc.  The view class is concerned with providing
     * layout inflation, and the display of data to that layout.  It also must handle click events and
     * other user input.  This is enough work for a view isn't it?  We can pass the hard work of data
     * manipulation, IO operations, network calls and their results/errors, and database handling off to
     * other classes and simply observe the results here.
     */
    private void observeViewModel() {
        //this is a fairly standard way to observe a viewmodels live data value
        //this can be applied to any livedata wrapped value, in any class that has
        //reference to a viewmodel providing livedata values for observation
        viewModel.getCurrentSubTotal().observe(this, subtotal -> {
            if(subtotal != null) {
                binding.txtSubTotal.setText(df.format(subtotal));
            } else {
                binding.txtSubTotal.setText(AppError.NO_VALUE.getLabel());
            }
        });
        viewModel.getCurrentTax().observe(this, taxAmount -> {
            if(taxAmount != null) {
                binding.txtTax.setText(df.format(taxAmount));
            } else {
                binding.txtTax.setText(AppError.NO_VALUE.getLabel());
            }
        });
        viewModel.getCurrentTotal().observe(this, total -> {
            if(total != null) {
                binding.txtTotal.setText(df.format(total));
            } else {
                binding.txtTotal.setText(AppError.NO_VALUE.getLabel());
            }
        });
        viewModel.getCurrentDiscountsTotal().observe(this, discountsTotals -> {
            binding.txtDiscounts.setText(df.format(discountsTotals));
            if (discountsTotals == null) {
                binding.txtDiscounts.setText(df.format(discountsTotals));
            } else {
                binding.txtDiscounts.setText(AppError.NO_VALUE.getLabel());
            }

        });
    }

    private void setOnClickListeners() {
        binding.btnAddToTotal.setOnClickListener(v -> {
                Double input = Utils.defaultCurrencyStringCheck(binding.etAdd.getText().toString().replace("$", ""));
                Double amount = CurrencyUtil.currencyValue(String.valueOf(input));
                System.out.println("%%%%%%  input is  " + input + " amount " + amount);
                viewModel.updateSubtotal(amount, true);

        });
        binding.btnSubtractToTotal.setOnClickListener(v-> {
            if(binding.etSubtract.getText() != null){
                Double amount = CurrencyUtil.currencyValue(binding.etSubtract.getText().toString().replace("$", ""));
                viewModel.updateSubtotal(amount, false);
            } else {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnSetDiscountAmount.setOnClickListener(v -> {
            if(binding.etDiscount.getText() != null){
                Double amount = CurrencyUtil.currencyValue(binding.etDiscount.getText().toString().replace("$", ""));
                viewModel.updateDiscount(amount);
            } else {
                Toast.makeText(this, "Please enter a discount amount", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        orderId = findViewById(R.id.tv_orderId);
        subTotal = findViewById(R.id.tv_orderSubTotal);
        uniqueId = findViewById(R.id.tv_orderUniqueId);
        firstName = findViewById(R.id.tv_clientFirstName);
        lastName = findViewById(R.id.tv_clientLastName);
        orderSent = findViewById(R.id.tv_isOrderSent);
        backButton = findViewById(R.id.btn_back);
        //set textwatchers
        CurrencyTextWatcher addWatcher = new CurrencyTextWatcher(binding.etDiscount);
        binding.etDiscount.addTextChangedListener(addWatcher);
        binding.etSubtract.addTextChangedListener(new CurrencyTextWatcher(binding.etSubtract));
        binding.etAdd.addTextChangedListener(new CurrencyTextWatcher(binding.etAdd));
        binding.etTax.addTextChangedListener(new CurrencyTextWatcher(binding.etTax));
    }
}