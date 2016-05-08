package taiji.org.donkeymgr;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import taiji.org.donkeymgr.dao.Donkey;
import taiji.org.donkeymgr.dao.DonkeyDao;
import taiji.org.donkeymgr.utils.DaoUtils;
import taiji.org.donkeymgr.utils.HandlerUtils;

public class EditActivity extends ToolBarActivity {

    boolean isAdd = false;
    Donkey donkey;
    private DonkeyDao daonkeyDao;

    EditText snEditText;
    EditText farmerEditText;
    EditText breedDddressEditText;
    EditText dealTimeEditText;
    EditText breedEditText;
    Spinner  sexSpinner;
    EditText ageWhenDealEditText;
    EditText ageWhenKillEditText;
    EditText feedEditText;
    EditText healthEditText;
    EditText breedStatusEditText;
    EditText killDepartmentEditText;
    EditText killPlaceEditText;
    EditText splitEditText;
    EditText processEditText;
    EditText qualityEditText;
    EditText killTimeEditText;
    EditText factoryTimeEditText;

    Button saveButton;
    Button clearButton;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        daonkeyDao = DaoUtils.getDonkeyDao(this);

        snEditText              = (EditText)findViewById(R.id.snEditText);
        farmerEditText          = (EditText)findViewById(R.id.farmerEditText);
        breedDddressEditText    = (EditText)findViewById(R.id.breedDddressEditText);
        dealTimeEditText        = (EditText)findViewById(R.id.dealTimeEditText);
        breedEditText           = (EditText)findViewById(R.id.breedEditText);
        sexSpinner              = (Spinner)findViewById(R.id.sexSpinner);
        ageWhenDealEditText     = (EditText)findViewById(R.id.ageWhenDealEditText);
        ageWhenKillEditText     = (EditText)findViewById(R.id.ageWhenKillEditText);
        feedEditText            = (EditText)findViewById(R.id.feedEditText);
        healthEditText          = (EditText)findViewById(R.id.healthEditText);
        breedStatusEditText     = (EditText)findViewById(R.id.breedStatusEditText);
        killDepartmentEditText  = (EditText)findViewById(R.id.killDepartmentEditText);
        killPlaceEditText       = (EditText)findViewById(R.id.killPlaceEditText);
        splitEditText           = (EditText)findViewById(R.id.splitEditText);
        processEditText         = (EditText)findViewById(R.id.processEditText);
        qualityEditText         = (EditText)findViewById(R.id.qualityEditText);
        killTimeEditText        = (EditText)findViewById(R.id.killTimeEditText);
        factoryTimeEditText     = (EditText)findViewById(R.id.factoryTimeEditText);

        saveButton = (Button)findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snEditText.getText().toString().length() == 0){
                    Toast.makeText(EditActivity.this, "请输入编号", Toast.LENGTH_SHORT).show();
                    return;
                }

                int sn = Integer.parseInt(snEditText.getText().toString());
                if(isAdd) {
                    if( DaoUtils.getDonkeyBySn(daonkeyDao, sn) != null ) {
                        Toast.makeText(EditActivity.this, "该编号已存在，请重新输入", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    donkey = new Donkey();
                }
                else {
                    donkey = DaoUtils.getDonkeyBySn(daonkeyDao, sn);
                }

                donkey.setSn(sn);
                donkey.setFarmer(farmerEditText.getText().toString());
                donkey.setBreedaddress(breedDddressEditText.getText().toString());
                donkey.setDealtime(dealTimeEditText.getText().toString());
                donkey.setBreed(breedEditText.getText().toString());
                donkey.setSex((String) sexSpinner.getSelectedItem());
                donkey.setAgewhendeal(ageWhenDealEditText.getText().toString());
                donkey.setAgewhenkill(ageWhenKillEditText.getText().toString());
                donkey.setFeedstatus(feedEditText.getText().toString());
                donkey.setHealthstatus(healthEditText.getText().toString());
                donkey.setBreedstatus(breedStatusEditText.getText().toString());
                donkey.setKilldepartment(killDepartmentEditText.getText().toString());
                donkey.setKillplace(killPlaceEditText.getText().toString());
                donkey.setSplitstatus(splitEditText.getText().toString());
                donkey.setProcessstatus(processEditText.getText().toString());
                donkey.setQualitystatyus(qualityEditText.getText().toString());
                donkey.setKilltime(killTimeEditText.getText().toString());
                donkey.setFactorytime(factoryTimeEditText.getText().toString());
                donkey.setSyncing(false);
                donkey.setDeleteflag(false);

                if (isAdd){
                    donkey.setVersion(1L);
                    donkey.setSyncver(0L);
                    donkey.setIdonserver(0L);
                    daonkeyDao.insert(donkey);

                    HandlerUtils.notifyNewRecord(donkey.getSn());
                    Toast.makeText(EditActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                }
                else{
                    donkey.setVersion( donkey.getVersion() + 1);
                    daonkeyDao.update(donkey);
                    HandlerUtils.updateUI();
                    Toast.makeText(EditActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clearButton = (Button)findViewById(R.id.clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snEditText.setText("");
                farmerEditText.setText("");
                breedDddressEditText.setText("");
                dealTimeEditText.setText("");
                breedEditText.setText("");
                sexSpinner.setSelection(0);
                ageWhenDealEditText.setText("");
                ageWhenKillEditText.setText("");
                feedEditText.setText("");
                healthEditText.setText("");
                breedStatusEditText.setText("");
                killDepartmentEditText.setText("");
                killPlaceEditText.setText("");
                killTimeEditText.setText("");
                splitEditText.setText("");
                processEditText.setText("");
                qualityEditText.setText("");
                factoryTimeEditText.setText("");
            }
        });

        dealTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this, dealTimeSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH) );
                datePickerDialog.show();
            }
        });

        killTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this, killTimeTimeSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH) );
                datePickerDialog.show();
            }
        });

        factoryTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this, factoryTimeSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH) );
                datePickerDialog.show();
            }
        });

        Intent intent = this.getIntent();
        String number = intent.getStringExtra("num");
        isAdd = intent.getBooleanExtra("isAdd", false);
        if (number == null)
            number = "";

        snEditText.setText(number);
        List<Donkey> donkeys = daonkeyDao.queryBuilder().where(DonkeyDao.Properties.Sn.eq(number)).list();
        if(donkeys.size() == 0)
            return;

        donkey = donkeys.get(0);
        farmerEditText.setText(donkey.getFarmer());
        breedDddressEditText.setText(donkey.getBreedaddress());
        dealTimeEditText.setText(donkey.getDealtime());
        breedEditText.setText(donkey.getBreed());
        ageWhenDealEditText.setText(donkey.getAgewhendeal());
        ageWhenKillEditText.setText(donkey.getAgewhenkill());
        feedEditText.setText(donkey.getFeedstatus());
        healthEditText.setText(donkey.getHealthstatus());
        breedStatusEditText.setText(donkey.getBreedstatus());
        killDepartmentEditText.setText(donkey.getKilldepartment());
        killPlaceEditText.setText(donkey.getKillplace());
        splitEditText.setText(donkey.getSplitstatus());
        processEditText.setText(donkey.getProcessstatus());
        qualityEditText.setText(donkey.getQualitystatyus());
        killTimeEditText.setText(donkey.getKilltime());
        factoryTimeEditText.setText(donkey.getFactorytime());

        String sex = donkey.getSex();
        for (int i = 0; i < sexSpinner.getCount(); i++) {
            String itemValue = (String)sexSpinner.getItemAtPosition(i);
            if (sex.compareToIgnoreCase(itemValue) == 0) {
                sexSpinner.setSelection(i);
                break;
            }
        }
    }

    private DatePickerDialog.OnDateSetListener dealTimeSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String date = year + "-" + monthOfYear + "-" + dayOfMonth;
            dealTimeEditText.setText(date);
        }
    };
    private DatePickerDialog.OnDateSetListener killTimeTimeSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String date = year + "-" + monthOfYear + "-" + dayOfMonth;
            killTimeEditText.setText(date);
        }
    };
    private DatePickerDialog.OnDateSetListener factoryTimeSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String date = year + "-" + monthOfYear + "-" + dayOfMonth;
            factoryTimeEditText.setText(date);
        }
    };
}
