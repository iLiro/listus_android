package com.youroff.listus.dialogs;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

import com.orm.SugarRecord;
import com.youroff.listus.R;
import com.youroff.listus.UseActivity;
import com.youroff.listus.databinding.DialogEditItemBinding;
import com.youroff.listus.databinding.DialogEditListBinding;
import com.youroff.listus.models.LItem;
import com.youroff.listus.validators.NumberValidator;
import com.youroff.listus.validators.TextLengthValidator;
import com.youroff.listus.validators.TextUniqueValidator;

import java.util.List;

public class LItemEditDialogFragment extends DialogFragment {

    UseActivity activity;
    LItem item;
    Boolean create;

    public static LItemEditDialogFragment newInstance(Integer pos) {
        LItemEditDialogFragment f = new LItemEditDialogFragment();

        Bundle args = new Bundle();
        if (pos != null) args.putInt("pos", pos);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        activity = (UseActivity) getActivity();
        Bundle b = getArguments();
        create = !b.containsKey("pos");
        if (create) {
            item = new LItem(activity.list, "", 0F);
        } else {
            item = activity.list.getItem(b.getInt("pos"));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        DialogEditItemBinding binding = DataBindingUtil.inflate(inflater, R.layout.dialog_edit_item, null, false);
        binding.setItem(item);

        builder.setTitle(create ? "Create Item" : "Edit Item")
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
            List<String> titles = activity.list.getTitles();
            titles.remove(item.getName().toLowerCase().trim());

            EditText nameText = (EditText) d.findViewById(R.id.nameText);
            nameText.addTextChangedListener(new TextLengthValidator(nameText, 1, null));
            nameText.addTextChangedListener(new TextUniqueValidator(nameText, titles));

            EditText priceText = (EditText) d.findViewById(R.id.priceText);
            priceText.addTextChangedListener(new NumberValidator(priceText));

            EditText qtyText = (EditText) d.findViewById(R.id.qtyText);
            qtyText.addTextChangedListener(new NumberValidator(qtyText));

            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener((v) -> {
                if (nameText.getError() == null
                        && priceText.getError() == null
                        && qtyText.getError() == null) {
                    item.setName(nameText.getText().toString());
                    item.setPrice(Float.parseFloat(priceText.getText().toString()));
                    item.setQty(Integer.parseInt(qtyText.getText().toString()));
                    if (create) {
                        activity.addItem(item);
                    } else {
                        SugarRecord.save(item);
                        activity.list.refreshItems();
                    }
                    d.dismiss();
                }
            });
        }
    }
}
