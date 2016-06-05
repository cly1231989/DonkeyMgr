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

import butterknife.BindView;
import butterknife.ButterKnife;
import taiji.org.donkeymgr.dao.Donkey;
import taiji.org.donkeymgr.dao.DonkeyDao;
import taiji.org.donkeymgr.utils.DaoUtils;
import taiji.org.donkeymgr.utils.HandlerUtils;

public class EditActivity extends ToolBarActivity {

    boolean isAdd = false;
    Donkey donkey;
    private DonkeyDao daonkeyDao;

    @BindView(R.id.snEditText) EditText snEditText;
    @BindView(R.id.farmerEditText) EditText farmerEditText;
    @BindView(R.id.breedAddressEditText) EditText breedAddressEditText;
    @BindView(R.id.dealTimeEditText) EditText dealTimeEditText;
    @BindView(R.id.supplierEditText) EditText supplierEditText;
    @BindView(R.id.supplyAddressEditText) EditText supplyAddressEditText;
    @BindView(R.id.supplyTimeEditText) EditText supplyTimeEditText;
    @BindView(R.id.breedSpinner) Spinner breedSpinner;
    @BindView(R.id.sexSpinner) Spinner  sexSpinner;
    //@BindView(R.id.ageWhenKillEditText) EditText ageWhenDealEditText;
    @BindView(R.id.ageWhenKillEditText) EditText ageWhenKillEditText;
    //@BindView EditText feedEditText;
    @BindView(R.id.feedPatternSpinner) Spinner feedPatternSpinner;
    @BindView(R.id.forageEditText) EditText forageEditText;
    @BindView(R.id.healthEditText) EditText healthEditText;
    @BindView(R.id.breedStatusEditText) EditText breedStatusEditText;
    @BindView(R.id.killDepartmentEditText)  EditText killDepartmentEditText;
    //@BindView(R.id.killPlaceEditText)  EditText killPlaceEditText;
    @BindView(R.id.killTimeEditText) EditText killTimeEditText;
    @BindView(R.id.freshKeepMethodSpinner) Spinner freshKeepMethodSpinner;
    @BindView(R.id.freshKeepTimeEditText) EditText freshKeepTimeEditText;
    //@BindView  EditText splitEditText;
    //@BindView  EditText processEditText;
    @BindView(R.id.qualitySpinner)  Spinner qualitySpinner;
    @BindView(R.id.QCEditText)  EditText QCEditText;
    @BindView(R.id.QAEditText)  EditText QAEditText;
    @BindView(R.id.furqualityEditText)  EditText furqualityEditText;
    @BindView(R.id.reservedEditText)  EditText reservedEditText;

    //@BindView  EditText factoryTimeEditText;

    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.clearButton) Button clearButton;
    private Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //View view = inflater.inflate(R.layout.activity_edit_page, container, false);
        ButterKnife.bind(this);

        daonkeyDao = DaoUtils.getDonkeyDao();
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
                donkey.setBreedaddress(breedAddressEditText.getText().toString());
                donkey.setDealtime(dealTimeEditText.getText().toString());
                donkey.setSupplier(supplierEditText.getText().toString());
                donkey.setSupplyaddress(supplyAddressEditText.getText().toString());
                donkey.setSupplytime(supplyTimeEditText.getText().toString());
                donkey.setBreed(breedSpinner.getSelectedItemPosition() + 1);
                donkey.setSex(sexSpinner.getSelectedItemPosition() + 1);
                //donkey.setBreed((String) breedSpinner.getSelectedItem());
                //donkey.setSex((String) sexSpinner.getSelectedItem());
                if( ageWhenKillEditText.getText().toString().length() != 0 )
                    donkey.setAgewhenkill(ageWhenKillEditText.getText().toString() + "岁");

                donkey.setFeedpattern((String)feedPatternSpinner.getSelectedItem());
                donkey.setForage(forageEditText.getText().toString());
                donkey.setHealthstatus(healthEditText.getText().toString());
                donkey.setBreedstatus(breedStatusEditText.getText().toString());
                donkey.setKilldepartment(killDepartmentEditText.getText().toString());
                //donkey.setKillplace(killPlaceEditText.getText().toString());
                donkey.setKilltime(killTimeEditText.getText().toString());
                donkey.setFreshkeepmethod((String)freshKeepMethodSpinner.getSelectedItem());
                donkey.setFreshkeeptime(freshKeepTimeEditText.getText().toString());
                donkey.setQualitystatus((String)qualitySpinner.getSelectedItem());
                donkey.setQC(QCEditText.getText().toString());
                donkey.setQA(QAEditText.getText().toString());
                donkey.setFurquality(furqualityEditText.getText().toString());
                donkey.setReserved(reservedEditText.getText().toString());

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

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                snEditText.setText("");
                farmerEditText.setText("");
                breedAddressEditText.setText("");
                dealTimeEditText.setText("");
                supplierEditText.setText("");
                supplyAddressEditText.setText("");
                supplyTimeEditText.setText("");
                breedSpinner.setSelection(0);
                sexSpinner.setSelection(0);
                ageWhenKillEditText.setText("");
                feedPatternSpinner.setSelection(0);
                forageEditText.setText("");
                healthEditText.setText("");
                breedStatusEditText.setText("");
                killDepartmentEditText.setText("");
                //killPlaceEditText.setText("");
                killTimeEditText.setText("");
                freshKeepMethodSpinner.setSelection(0);
                freshKeepTimeEditText.setText("");
                qualitySpinner.setSelection(0);
                QCEditText.setText("");
                QAEditText.setText("");
                furqualityEditText.setText("");
                reservedEditText.setText("");
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

        supplyTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this,  supplyTimeSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
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

//        factoryTimeEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this, factoryTimeSet, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
//                        calendar.get(Calendar.DAY_OF_MONTH) );
//                datePickerDialog.show();
//            }
//        });

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
        breedAddressEditText.setText(donkey.getBreedaddress());
        dealTimeEditText.setText(donkey.getDealtime());
        supplierEditText.setText(donkey.getSupplier());
        supplyAddressEditText.setText(donkey.getSupplyaddress());
        supplyTimeEditText.setText(donkey.getSupplytime());
        breedSpinner.setSelection(donkey.getBreed() - 1);
        sexSpinner.setSelection(donkey.getSex() - 1);
        //updateSpinner(breedSpinner, donkey.getBreed());
        //updateSpinner(sexSpinner, donkey.getSex());
        if(donkey.getAgewhenkill() != null && donkey.getAgewhenkill().length() >= 2)
            ageWhenKillEditText.setText(donkey.getAgewhenkill().substring(0,donkey.getAgewhenkill().length()-1 ));

        updateSpinner(feedPatternSpinner, donkey.getFeedpattern());
        forageEditText.setText(donkey.getForage());
        healthEditText.setText(donkey.getHealthstatus());
        breedStatusEditText.setText(donkey.getBreedstatus());
        killDepartmentEditText.setText(donkey.getKilldepartment());
        //killPlaceEditText.setText(donkey.getKillplace());
        killTimeEditText.setText(donkey.getKilltime());
        updateSpinner(freshKeepMethodSpinner, donkey.getFreshkeepmethod());
        freshKeepTimeEditText.setText(donkey.getFreshkeeptime());
        updateSpinner(qualitySpinner, donkey.getQualitystatus());
        QCEditText.setText(donkey.getQC());
        QAEditText.setText(donkey.getQA());
        furqualityEditText.setText(donkey.getFurquality());
        reservedEditText.setText(donkey.getReserved());
    }

    private void updateSpinner(Spinner spinner, String text){
        if (text == null){
            spinner.setSelection(0);
            return;
        }

        for (int i = 0; i < spinner.getCount(); i++) {
            String itemValue = (String)spinner.getItemAtPosition(i);
            if (text.compareToIgnoreCase(itemValue) == 0) {
                spinner.setSelection(i);
                return;
            }
        }

        spinner.setSelection(0);
    }

    private DatePickerDialog.OnDateSetListener dealTimeSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
            dealTimeEditText.setText(date);
        }
    };
    private DatePickerDialog.OnDateSetListener killTimeTimeSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
            killTimeEditText.setText(date);
        }
    };

    private DatePickerDialog.OnDateSetListener  supplyTimeSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
            supplyTimeEditText.setText(date);
        }
    };

    private DatePickerDialog.OnDateSetListener factoryTimeSet = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String date = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
            //factoryTimeEditText.setText(date);
        }
    };
}
