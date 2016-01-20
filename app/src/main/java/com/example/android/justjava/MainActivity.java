package com.example.android.justjava;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends ActionBarActivity {

    int numberOfCoffees = 0;
    double price = 0;
    boolean hasChocolate;
    boolean hasWhippedCream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText nameField = (EditText) findViewById(R.id.name_field);
        nameField.setCursorVisible(true);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                ((EditText) v).setCursorVisible(true);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    ((EditText) v).setCursorVisible(false);

                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
    /**
     * This method is called when the + button is clicked.
     */
    public void increment(View view) {
        if (numberOfCoffees <= 49) {
            numberOfCoffees = numberOfCoffees + 1;
            display(numberOfCoffees);
            displayPrice(numberOfCoffees,hasWhippedCream,hasChocolate);
        }
        else {
            Toast.makeText(this, getString(R.string.fifty_cups), Toast.LENGTH_SHORT).show();
        }

    }
    /**
     * This method is called when the - button is clicked.
     */
    public void decrement(View view) {
        if (numberOfCoffees >= 1) {
            numberOfCoffees = numberOfCoffees - 1;
            display(numberOfCoffees);
            displayPrice(numberOfCoffees,hasWhippedCream,hasChocolate);
        }
        else {
            Toast.makeText(this, getString(R.string.negative_cups), Toast.LENGTH_SHORT).show();
        }
    }
    public void addWhippedCream(View view) {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        hasWhippedCream = whippedCreamCheckBox.isChecked();
        displayPrice(numberOfCoffees,hasWhippedCream,hasChocolate);
        }

    public void addChocolate(View view) {
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        hasChocolate = chocolateCheckBox.isChecked();
        displayPrice(numberOfCoffees,hasWhippedCream,hasChocolate);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(String message, EditText nameField, CheckBox whippedCreamCheckBox, CheckBox chocolateCheckBox) {
        TextView confirmTextView = (TextView) findViewById(R.id.confirmation);
        confirmTextView.setText(message);
        Button emailButton = (Button) findViewById(R.id.email_confirm);
        emailButton.setVisibility(View.VISIBLE);
        Button orderButton = (Button) findViewById(R.id.order);
        orderButton.setText(getString(R.string.done));
        orderButton.setTextSize(20);
        orderButton.setTextColor(android.graphics.Color.WHITE);
        orderButton.setBackgroundColor(Color.TRANSPARENT);
        orderButton.setClickable(false);
        Button incrementButton = (Button) findViewById(R.id.increment);
        incrementButton.setClickable(false);
        Button decrementButton = (Button) findViewById(R.id.decrement);
        decrementButton.setClickable(false);
        nameField.setFocusable(false);
        nameField.setFocusableInTouchMode(false);
        whippedCreamCheckBox.setClickable(false);
        chocolateCheckBox.setClickable(false);
        TextView priceTextView = (TextView)findViewById(R.id.price_text_view);
        priceTextView.setVisibility(View.INVISIBLE);
    }


    public void composeEmail(String subject, String text) {
        Intent emailSummary = new Intent(Intent.ACTION_SENDTO);
        emailSummary.setData(Uri.parse("mailto:"));
        emailSummary.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailSummary.putExtra(Intent.EXTRA_TEXT, text);
        if (emailSummary.resolveActivity(getPackageManager()) != null) {
            startActivity(emailSummary);
        }
    }

    public void confirmEmail(View view) {
        EditText nameField = (EditText)findViewById(R.id.name_field);
        String name = nameField.getText().toString();
        TextView confirmField = (TextView)findViewById(R.id.confirmation);
        String confirmMessage = confirmField.getText().toString();
        composeEmail(getString(R.string.email_subject) + name, confirmMessage);
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    public void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the given price on the screen.
     */
    public void displayPrice(int quantity,boolean hasWhippedCream, boolean hasChocolate) {
        double priceChocolate = 0.5;
        double priceWhippedCream = 0.5;
        price = quantity*3;
        if (hasWhippedCream) {
            price += priceWhippedCream * quantity;
        }
        if (hasChocolate) {
            price += priceChocolate*quantity;
        }
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(getString(R.string.price) +": " + NumberFormat.getCurrencyInstance().format(price));
    }

    /**
     * This method displays the total and order confirmation on the screen.
     */
    public void displayOrderSummary(View view) {
        createOrderSummary(numberOfCoffees, price);
    }
    public void createOrderSummary(int quantity, double total){
        EditText nameField = (EditText)findViewById(R.id.name_field);
        String name = nameField.getText().toString();
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        boolean hasChocolate = chocolateCheckBox.isChecked();
        String confirmMessage;
        if (name.trim().length() == 0) {
            Toast.makeText(this, getString(R.string.name_required), Toast.LENGTH_SHORT).show();
        }
        else{
            if (numberOfCoffees == 0) {
                Toast.makeText(this, getString(R.string.make_selection), Toast.LENGTH_SHORT).show();

            } else {
                confirmMessage = getString(R.string.name) + ": " + name + "\n" +getString(R.string.quantity)  + numberOfCoffees;
                if (hasWhippedCream) {
                    confirmMessage += "\n" + getString(R.string.with_whipped_cream);
                }
                if (hasChocolate) {
                    confirmMessage += "\n" + getString(R.string.with_chocolate);
                }
                confirmMessage += "\n" + getString(R.string.total) + ": " + NumberFormat.getCurrencyInstance().format(price) + "\n\n" + getString(R.string.thank_you);
                submitOrder(confirmMessage, nameField, whippedCreamCheckBox, chocolateCheckBox);
            }
        }
    }

}
