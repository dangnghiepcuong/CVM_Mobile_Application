package com.example.cvm_mobile_application.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cvm_mobile_application.R;

public class CustomDialog {
    private Context context;
    private TextView tvTitle;
    private TextView tvContent;
    private Button btnCancel;
    private Button btnConfirm;
    private Dialog dialog;

    public CustomDialog(Context context) {
        this.context = context;

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_confirm);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.card_white_radius10);

        tvTitle = dialog.findViewById(R.id.tv_title);
        tvContent = dialog.findViewById(R.id.tv_content);
        btnCancel = dialog.findViewById(R.id.btn_cancel);
        btnConfirm = dialog.findViewById(R.id.btn_confirm);
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public void setBtnCancel(Button btnCancel) {
        this.btnCancel = btnCancel;
    }

    public Button getBtnConfirm() {
        return btnConfirm;
    }

    public void setBtnConfirm(Button btnConfirm) {
        this.btnConfirm = btnConfirm;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setViewListener(OnClickButtonListener onClickButtonListener) {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonListener.onClickCancel();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickButtonListener.onClickConfirm();
            }
        });
    }

    public void showDialog(String title, String content) {


        tvTitle.setText(title);
        tvContent.setText(content);

        dialog.show();
    }

    public interface OnClickButtonListener {
        public void onClickCancel();
        public void onClickConfirm();
    }
}
