package com.youroff.listus.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

import com.orm.SugarRecord;
import com.youroff.listus.R;
import com.youroff.listus.UseActivity;
import com.youroff.listus.databinding.DialogEditListBinding;
import com.youroff.listus.models.LList;
import com.youroff.listus.validators.NumberValidator;
import com.youroff.listus.validators.TextLengthValidator;

public class LListEditDialogFragment extends DialogFragment {

    UseActivity activity;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        activity = (UseActivity) getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        DialogEditListBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_list, null, false);
        binding.setList(activity.list);

        builder.setTitle("Edit List")
                .setView(binding.getRoot())
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", null);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            EditText nameText = (EditText) d.findViewById(R.id.nameText);
            nameText.addTextChangedListener(new TextLengthValidator(nameText, 1, null));

            EditText budgetNumber = (EditText) d.findViewById(R.id.budgetNumber);
            budgetNumber.addTextChangedListener(new NumberValidator(budgetNumber));

            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener((v) -> {
                if (nameText.getError() == null && budgetNumber.getError() == null) {
                    activity.list.setName(nameText.getText().toString());
                    activity.list.setBudget(Float.parseFloat(budgetNumber.getText().toString()));
                    SugarRecord.save(activity.list);
                    activity.list.refreshItems();
                    d.dismiss();
                }
            });
        }
    }
}
