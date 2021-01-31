/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.excellentsystem.jagobangunpersadafx.View;

import com.excellentsystem.jagobangunpersadafx.DAO.KategoriTransaksiDAO;
import com.excellentsystem.jagobangunpersadafx.DAO.KeuanganDAO;
import com.excellentsystem.jagobangunpersadafx.DAO.PropertyDAO;
import com.excellentsystem.jagobangunpersadafx.Function;
import static com.excellentsystem.jagobangunpersadafx.Function.getDateCellAkhir;
import static com.excellentsystem.jagobangunpersadafx.Function.getDateCellMulai;
import static com.excellentsystem.jagobangunpersadafx.Function.getTreeTableCell;
import com.excellentsystem.jagobangunpersadafx.Koneksi;
import com.excellentsystem.jagobangunpersadafx.Main;
import static com.excellentsystem.jagobangunpersadafx.Main.rp;
import static com.excellentsystem.jagobangunpersadafx.Main.sistem;
import static com.excellentsystem.jagobangunpersadafx.Main.tgl;
import static com.excellentsystem.jagobangunpersadafx.Main.tglBarang;
import static com.excellentsystem.jagobangunpersadafx.Main.tglLengkap;
import static com.excellentsystem.jagobangunpersadafx.Main.tglSql;
import com.excellentsystem.jagobangunpersadafx.Model.KategoriTransaksi;
import com.excellentsystem.jagobangunpersadafx.Model.Keuangan;
import com.excellentsystem.jagobangunpersadafx.Model.Otoritas;
import com.excellentsystem.jagobangunpersadafx.Model.Property;
import com.excellentsystem.jagobangunpersadafx.Service.Service;
import com.excellentsystem.jagobangunpersadafx.View.Dialog.DetailPembangunanController;
import com.excellentsystem.jagobangunpersadafx.View.Dialog.DetailPembelianTanahController;
import com.excellentsystem.jagobangunpersadafx.View.Dialog.DetailPropertyController;
import com.excellentsystem.jagobangunpersadafx.View.Dialog.DetailRealisasiProyekController;
import com.excellentsystem.jagobangunpersadafx.View.Dialog.DetailTerimaDownPaymentController;
import com.excellentsystem.jagobangunpersadafx.View.Dialog.DetailTerimaPencairanKPRController;
import com.excellentsystem.jagobangunpersadafx.View.Dialog.DetailTerimaTandaJadiController;
import com.excellentsystem.jagobangunpersadafx.View.Dialog.DetailTransaksiKeuanganController;
import com.excellentsystem.jagobangunpersadafx.View.Dialog.EditController;
import com.excellentsystem.jagobangunpersadafx.View.Dialog.MessageController;
import com.excellentsystem.jagobangunpersadafx.View.Dialog.TransferKeuanganController;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Xtreme
 */
public class KeuanganController {

    @FXML
    private TreeTableView<Keuangan> keuanganTable;
    @FXML
    private TreeTableColumn<Keuangan, String> noKeuanganColumn;
    @FXML
    private TreeTableColumn<Keuangan, String> tipeKeuanganColumn;
    @FXML
    private TreeTableColumn<Keuangan, String> kodePropertyColumn;
    @FXML
    private TreeTableColumn<Keuangan, String> kategoriColumn;
    @FXML
    private TreeTableColumn<Keuangan, String> deskripsiColumn;
    @FXML
    private TreeTableColumn<Keuangan, Number> jumlahRpColumn;
    @FXML
    private TreeTableColumn<Keuangan, Number> totalImageColumn;
    @FXML
    private TreeTableColumn<Keuangan, String> kodeUserColumn;
    @FXML
    private TreeTableColumn<Keuangan, String> tglInputColumn;
    @FXML
    private TextField searchField;
    @FXML
    private Label saldoAkhirField;
    @FXML
    private DatePicker tglAwalPicker;
    @FXML
    private DatePicker tglAkhirPicker;
    @FXML
    private ComboBox<String> TipeKeuanganCombo;

    private final TreeItem<Keuangan> root = new TreeItem<>();
    private ObservableList<Keuangan> allKeuangan = FXCollections.observableArrayList();
    private ObservableList<Keuangan> filterData = FXCollections.observableArrayList();
    private ObservableList<KategoriTransaksi> allKategori = FXCollections.observableArrayList();
    private Main mainApp;

    public void initialize() {
        noKeuanganColumn.setCellValueFactory(param -> param.getValue().getValue().noKeuanganProperty());
        noKeuanganColumn.setCellFactory(col -> Function.getWrapTreeTableCell(noKeuanganColumn));

        tipeKeuanganColumn.setCellValueFactory(param -> param.getValue().getValue().tipeKeuanganProperty());
        tipeKeuanganColumn.setCellFactory(col -> Function.getWrapTreeTableCell(tipeKeuanganColumn));

        kategoriColumn.setCellValueFactory(param -> param.getValue().getValue().kategoriProperty());
        kategoriColumn.setCellFactory(col -> Function.getWrapTreeTableCell(kategoriColumn));

        deskripsiColumn.setCellValueFactory(param -> param.getValue().getValue().deskripsiProperty());
        deskripsiColumn.setCellFactory(col -> Function.getWrapTreeTableCell(deskripsiColumn));

        kodePropertyColumn.setCellValueFactory(x -> {
            if (x.getValue().getValue().getKodeProperty().equals("")) {
                return null;
            } else {
                return new SimpleStringProperty(x.getValue().getValue().getProperty().getNamaProperty());
            }
        });
        kodePropertyColumn.setCellFactory(col -> Function.getWrapTreeTableCell(kodePropertyColumn));

        kodeUserColumn.setCellValueFactory(param -> param.getValue().getValue().kodeUserProperty());
        kodeUserColumn.setCellFactory(col -> Function.getWrapTreeTableCell(kodeUserColumn));

        tglInputColumn.setCellValueFactory(cellData -> {
            try {
                return new SimpleStringProperty(tglLengkap.format(tglSql.parse(cellData.getValue().getValue().getTglInput())));
            } catch (Exception ex) {
                return null;
            }
        });
        tglInputColumn.setCellFactory(col -> Function.getWrapTreeTableCell(tglInputColumn));
        tglInputColumn.setComparator(Function.sortDate(tglLengkap));

        jumlahRpColumn.setCellValueFactory(param -> param.getValue().getValue().jumlahRpProperty());
        jumlahRpColumn.setCellFactory(col -> getTreeTableCell(rp));
        
        totalImageColumn.setCellValueFactory(param -> param.getValue().getValue().totalImageProperty());
        totalImageColumn.setCellFactory(col -> getTreeTableCell(rp));

        tglAwalPicker.setConverter(Function.getTglConverter());
        tglAwalPicker.setValue(LocalDate.now().minusWeeks(1));
        tglAwalPicker.setDayCellFactory((final DatePicker datePicker) -> getDateCellMulai(tglAkhirPicker));
        tglAkhirPicker.setConverter(Function.getTglConverter());
        tglAkhirPicker.setValue(LocalDate.now());
        tglAkhirPicker.setDayCellFactory((final DatePicker datePicker) -> getDateCellAkhir(tglAwalPicker));
        ContextMenu rowMenu = new ContextMenu();
        MenuItem addNew = new MenuItem("Add New Keuangan");
        addNew.setOnAction(e -> {
            showNewKeuangan();
        });
        MenuItem transfer = new MenuItem("Transfer Keuangan");
        transfer.setOnAction(e -> {
            showTransfer();
        });
        MenuItem refresh = new MenuItem("Refresh");
        refresh.setOnAction(e -> {
            getKeuangan();
        });
        for (Otoritas o : sistem.getUser().getOtoritas()) {
            if (o.getJenis().equals("Add New Keuangan") && o.isStatus()) {
                rowMenu.getItems().add(addNew);
            }
            if (o.getJenis().equals("Transfer Keuangan") && o.isStatus()) {
                rowMenu.getItems().add(transfer);
            }
        }
        rowMenu.getItems().add(refresh);
        keuanganTable.setContextMenu(rowMenu);
        keuanganTable.setRowFactory(ttv -> {
            TreeTableRow<Keuangan> row = new TreeTableRow<Keuangan>() {
                @Override
                public void updateItem(Keuangan item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setContextMenu(null);
                    } else {
                        if (item.getNoKeuangan().startsWith("KK")) {
                            ContextMenu rowMenu = new ContextMenu();
                            MenuItem addNew = new MenuItem("Add New Keuangan");
                            addNew.setOnAction(e -> {
                                showNewKeuangan();
                            });
                            MenuItem detail = new MenuItem("Detail Transaksi Keuangan");
                            detail.setOnAction((e) -> {
                                detailTransaksiKeuangan(item);
                            });
                            MenuItem transfer = new MenuItem("Transfer Keuangan");
                            transfer.setOnAction(e -> {
                                showTransfer();
                            });
                            MenuItem edit = new MenuItem("Edit Keterangan");
                            edit.setOnAction((e) -> {
                                edit(item);
                            });
                            MenuItem batal = new MenuItem("Batal Keuangan");
                            batal.setOnAction(e -> {
                                batal(item);
                            });
                            MenuItem property = new MenuItem("Detail Property");
                            property.setOnAction((ActionEvent event) -> {
                                detailProperty(item.getProperty());
                            });
                            MenuItem pembelian = new MenuItem("Detail Pembelian");
                            pembelian.setOnAction((ActionEvent event) -> {
                                detailPembelian(item.getProperty());
                            });
                            MenuItem pembangunan = new MenuItem("Detail Pembangunan");
                            pembangunan.setOnAction((ActionEvent event) -> {
                                detailPembangunan(item.getDeskripsi().split(" - ")[1]);
                            });
                            MenuItem realisasi = new MenuItem("Detail Realisasi Proyek");
                            realisasi.setOnAction((ActionEvent event) -> {
                                detailRealisasi(item.getDeskripsi().split(" - ")[1], item.getDeskripsi().split(" - ")[2]);
                            });
                            MenuItem tj = new MenuItem("Detail Terima Tanda Jadi");
                            tj.setOnAction((ActionEvent event) -> {
                                detailTerimaTandaJadi(item.getDeskripsi().split(" - ")[1]);
                            });
                            MenuItem dp = new MenuItem("Detail Terima Down Payment");
                            dp.setOnAction((ActionEvent event) -> {
                                detailTerimaDownPayment(item.getDeskripsi().split(" - ")[1]);
                            });
                            MenuItem kpr = new MenuItem("Detail Terima Pencairan KPR");
                            kpr.setOnAction((ActionEvent event) -> {
                                detailTerimaPencairanKPR(item.getProperty());
                            });
                            MenuItem refresh = new MenuItem("Refresh");
                            refresh.setOnAction(e -> {
                                getKeuangan();
                            });
                            if (item.getKategori().equals("Pembelian Tanah")) {
                                rowMenu.getItems().add(pembelian);
                            } else if (item.getKategori().equals("Pembangunan")) {
                                rowMenu.getItems().add(pembangunan);
                            } else if (item.getKategori().equals("Realisasi Proyek")) {
                                rowMenu.getItems().add(realisasi);
                            } else if (item.getKategori().equals("Terima Tanda Jadi")) {
                                rowMenu.getItems().add(tj);
                            } else if (item.getKategori().equals("Terima Down Payment")) {
                                rowMenu.getItems().add(dp);
                            } else if (item.getKategori().equals("Terima Pencairan KPR")) {
                                rowMenu.getItems().add(kpr);
                            }
                            Boolean status = false;
                            for (KategoriTransaksi k : allKategori) {
                                if (item.getKategori().equals(k.getKodeKategori()) || item.getKategori().startsWith("Transfer")) {
                                    status = true;
                                }
                            }
                            for (Otoritas o : sistem.getUser().getOtoritas()) {
                                if (o.getJenis().equals("Add New Keuangan") && o.isStatus()) {
                                    rowMenu.getItems().add(addNew);
                                }
                                if (o.getJenis().equals("Detail Transaksi Keuangan") && o.isStatus()) {
                                    rowMenu.getItems().add(detail);
                                }
                                if (o.getJenis().equals("Transfer Keuangan") && o.isStatus()) {
                                    rowMenu.getItems().add(transfer);
                                }
                                if (o.getJenis().equals("Edit Keterangan") && o.isStatus() && status) {
                                    rowMenu.getItems().add(edit);
                                }
                                if (o.getJenis().equals("Batal Keuangan") && o.isStatus() && status) {
                                    rowMenu.getItems().add(batal);
                                }
                            }
                            if (item.getProperty() != null) {
                                rowMenu.getItems().add(property);
                            }
                            rowMenu.getItems().add(refresh);
                            setContextMenu(rowMenu);
                        }
                    }
                }
            };
            return row;
        });
        allKeuangan.addListener((ListChangeListener.Change<? extends Keuangan> change) -> {
            searchKeuangan();
        });
        searchField.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    searchKeuangan();
                });
        filterData.addAll(allKeuangan);
    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
        Task<List<KategoriTransaksi>> task = new Task<List<KategoriTransaksi>>() {
            @Override
            public List<KategoriTransaksi> call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    return KategoriTransaksiDAO.getAll(con);
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((WorkerStateEvent e) -> {
            try {
                mainApp.closeLoading();
                allKategori.addAll(task.getValue());
                TipeKeuanganCombo.setItems(Function.getTipeKeuangan());
                TipeKeuanganCombo.getSelectionModel().selectFirst();
                getKeuangan();
            } catch (Exception ex) {
                mainApp.showMessage(Modality.NONE, "Error", ex.toString());
            }
        });
        task.setOnFailed((e) -> {
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
            mainApp.closeLoading();
        });
        new Thread(task).start();
    }
    private double saldoAwal = 0;
    private double saldoAkhir = 0;

    @FXML
    private void getKeuangan() {
        Task<List<Keuangan>> task = new Task<List<Keuangan>>() {
            @Override
            public List<Keuangan> call() throws Exception {
                try (Connection con = Koneksi.getConnection()) {
                    if (TipeKeuanganCombo.getSelectionModel().getSelectedItem() != null) {
                        List<String> statusProperty = new ArrayList<>();
                        statusProperty.add("Combined");
                        statusProperty.add("Mapped");
                        statusProperty.add("Deleted");
                        statusProperty.add("Available");
                        statusProperty.add("Reserved");
                        statusProperty.add("Sold");
                        List<Property> listProperty = PropertyDAO.getAllByStatus(con, statusProperty);
                        List<Keuangan> listKeuangan = KeuanganDAO.getAllByTipeKeuanganAndDate(con,
                                TipeKeuanganCombo.getSelectionModel().getSelectedItem(), tglAwalPicker.getValue().toString(),
                                tglAkhirPicker.getValue().toString());
                        for (Keuangan k : listKeuangan) {
                            if (!k.getKodeProperty().equals("")) {
                                for (Property p : listProperty) {
                                    if (k.getKodeProperty().equals(p.getKodeProperty())) {
                                        k.setProperty(p);
                                    }
                                }
                            }
                        }
                        saldoAwal = KeuanganDAO.getSaldoAwalByDateAndTipeKeuangan(
                                con, tglAwalPicker.getValue().toString(), TipeKeuanganCombo.getSelectionModel().getSelectedItem());
                        saldoAkhir = KeuanganDAO.getSaldoAkhirByDateAndTipeKeuangan(
                                con, tglAkhirPicker.getValue().toString(), TipeKeuanganCombo.getSelectionModel().getSelectedItem());
                        return listKeuangan;
                    } else {
                        return null;
                    }
                }
            }
        };
        task.setOnRunning((e) -> {
            mainApp.showLoadingScreen();
        });
        task.setOnSucceeded((WorkerStateEvent e) -> {
            try {
                mainApp.closeLoading();
                allKeuangan.clear();
                allKeuangan.addAll(task.getValue());
                saldoAkhirField.setText(rp.format(saldoAkhir));
            } catch (Exception ex) {
                mainApp.showMessage(Modality.NONE, "Error", ex.toString());
            }
        });
        task.setOnFailed((e) -> {
            mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
            mainApp.closeLoading();
        });
        new Thread(task).start();
    }

    private Boolean checkColumn(String column) {
        if (column != null) {
            if (column.toLowerCase().contains(searchField.getText().toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void searchKeuangan() {
        try {
            filterData.clear();
            for (Keuangan temp : allKeuangan) {
                if (searchField.getText() == null || searchField.getText().equals("")) {
                    filterData.add(temp);
                } else {
                    if (checkColumn(temp.getNoKeuangan())
                            || checkColumn(tgl.format(tglBarang.parse(temp.getTglKeuangan())))
                            || checkColumn(tglLengkap.format(tglSql.parse(temp.getTglInput())))
                            || checkColumn(temp.getTipeKeuangan())
                            || checkColumn(temp.getKodeProperty())
                            || checkColumn(temp.getKategori())
                            || checkColumn(temp.getDeskripsi())
                            || checkColumn(rp.format(temp.getJumlahRp()))
                            || checkColumn(temp.getKodeUser())) {
                        filterData.add(temp);
                    } else {
                        if (temp.getProperty() != null) {
                            if (checkColumn(temp.getProperty().getNamaProperty())) {
                                filterData.add(temp);
                            }
                        }
                    }
                }
            }
            setTable();
        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }

    private void setTable() throws Exception {
        if (keuanganTable.getRoot() != null) {
            keuanganTable.getRoot().getChildren().clear();
        }
        List<String> tanggal = new ArrayList<>();
        for (Keuangan temp : filterData) {
            if (!tanggal.contains(temp.getTglKeuangan())) {
                tanggal.add(temp.getTglKeuangan());
            }
        }
        for (String t : tanggal) {
            Keuangan tglKeu = new Keuangan();
            tglKeu.setNoKeuangan(tgl.format(tglBarang.parse(t)));
            tglKeu.setJumlahRp(saldoAwal);
            tglKeu.setKodeProperty("");
            TreeItem<Keuangan> parent = new TreeItem<>(tglKeu);
            int totalImage = 0;
            for (Keuangan keu : filterData) {
                if (t.equals(keu.getTglKeuangan())) {
                    Boolean status = true;
                    for (TreeItem<Keuangan> temp : parent.getChildren()) {
                        if (temp.getValue().getNoKeuangan().equals(keu.getNoKeuangan())) {
                            status = false;
                        }
                    }
                    if (status) {
                        Boolean havechild = false;
                        for (Keuangan k : filterData) {
                            if (keu.getNoKeuangan().equals(k.getNoKeuangan()) && !k.equals(keu)) {
                                havechild = true;
                            }
                        }
                        if (havechild) {
                            Keuangan kk = new Keuangan();
                            kk.setNoKeuangan(keu.getNoKeuangan());
                            kk.setTglKeuangan(keu.getTglKeuangan());
                            kk.setTipeKeuangan(keu.getTipeKeuangan());
                            kk.setKategori(keu.getKategori());
                            kk.setKodeProperty("");
                            kk.setDeskripsi(keu.getDeskripsi());
                            kk.setTotalImage(keu.getTotalImage());
                            kk.setKodeUser(keu.getKodeUser());
                            kk.setTglInput(keu.getTglInput());
                            kk.setStatus(keu.getStatus());
                            kk.setTglBatal(keu.getTglBatal());
                            kk.setUserBatal(keu.getUserBatal());
                            TreeItem<Keuangan> child = new TreeItem<>(kk);
                            double total = 0;
                            for (Keuangan k : filterData) {
                                if (keu.getNoKeuangan().equals(k.getNoKeuangan())) {
                                    total = total + k.getJumlahRp();
                                    child.getChildren().add(new TreeItem<>(k));
                                }
                            }
                            child.getValue().setJumlahRp(total);
                            parent.getChildren().add(child);
                        } else {
                            parent.getChildren().add(new TreeItem<>(keu));
                        }
                    }
                    saldoAwal = saldoAwal + keu.getJumlahRp();
                    totalImage = totalImage + keu.getTotalImage();
                }
            }
            parent.getValue().setTotalImage(totalImage);
            root.getChildren().add(parent);
        }
        keuanganTable.setRoot(root);
    }

    private void showTransfer() {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/TransferKeuangan.fxml");
            TransferKeuanganController x = loader.getController();
            x.setMainApp(mainApp, mainApp.MainStage, stage);
            x.saveButton.setOnAction((ActionEvent ev) -> {
                if (x.dariCombo.getSelectionModel().getSelectedItem() == null) {
                    mainApp.showMessage(Modality.NONE, "Error", "Kode keuangan asal transfer belum dipilih");
                } else if (x.keCombo.getSelectionModel().getSelectedItem() == null) {
                    mainApp.showMessage(Modality.NONE, "Error", "Kode keuangan tujuan transfer belum dipilih");
                } else if (x.dariCombo.getSelectionModel().getSelectedItem().equals(x.keCombo.getSelectionModel().getSelectedItem())) {
                    mainApp.showMessage(Modality.NONE, "Error", "Asal dan tujuan transfer tidak boleh sama");
                } else if (x.jumlahRpField.getText().equals("0")) {
                    mainApp.showMessage(Modality.NONE, "Warning", "Jumlah Rp yang akan ditransfer masih kosong");
                } else {
                    Task<String> task = new Task<String>() {
                        @Override
                        public String call() throws Exception {
                            try (Connection con = Koneksi.getConnection()) {
                                Keuangan d = new Keuangan();
                                d.setNoKeuangan(null);
                                d.setTglKeuangan(tglBarang.format(new Date()));
                                d.setTipeKeuangan(x.dariCombo.getSelectionModel().getSelectedItem());
                                d.setKategori("Transfer Keuangan " + x.dariCombo.getSelectionModel().getSelectedItem() + "-" + x.keCombo.getSelectionModel().getSelectedItem());
                                d.setKodeProperty("");
                                d.setDeskripsi(x.keteranganField.getText());
                                d.setJumlahRp(-Double.parseDouble(x.jumlahRpField.getText().replaceAll(",", "")));
                                d.setTotalImage(0);
                                d.setKodeUser(sistem.getUser().getUsername());
                                d.setTglInput(tglSql.format(new Date()));
                                d.setStatus("true");
                                d.setTglBatal("2000-01-01 00:00:00");
                                d.setUserBatal("");

                                Keuangan t = new Keuangan();
                                t.setNoKeuangan(null);
                                t.setTglKeuangan(tglBarang.format(new Date()));
                                t.setTipeKeuangan(x.keCombo.getSelectionModel().getSelectedItem());
                                t.setKategori("Transfer Keuangan " + x.dariCombo.getSelectionModel().getSelectedItem() + "-" + x.keCombo.getSelectionModel().getSelectedItem());
                                t.setKodeProperty("");
                                t.setDeskripsi(x.keteranganField.getText());
                                t.setJumlahRp(Double.parseDouble(x.jumlahRpField.getText().replaceAll(",", "")));
                                t.setTotalImage(0);
                                t.setKodeUser(sistem.getUser().getUsername());
                                t.setTglInput(tglSql.format(new Date()));
                                t.setStatus("true");
                                t.setTglBatal("2000-01-01 00:00:00");
                                t.setUserBatal("");
                                List<Keuangan> z = new ArrayList<>();
                                z.add(d);
                                z.add(t);

                                return Service.saveAllTransaksiKeuangan(con, z);
                            }
                        }
                    };
                    task.setOnRunning((e) -> {
                        mainApp.showLoadingScreen();
                    });
                    task.setOnSucceeded((WorkerStateEvent e) -> {
                        try {
                            mainApp.closeLoading();
                            if (task.getValue().equals("true")) {
                                mainApp.closeDialog(mainApp.MainStage, stage);
                                mainApp.showMessage(Modality.NONE, "Success", "Transfer Keuangan berhasil disimpan");
                            } else {
                                mainApp.showMessage(Modality.NONE, "Error", task.getValue());
                            }
                        } catch (Exception ex) {
                            mainApp.showMessage(Modality.NONE, "Error", ex.toString());
                        }
                    });
                    task.setOnFailed((e) -> {
                        mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                        mainApp.closeLoading();
                    });
                    new Thread(task).start();
                }
            });

        } catch (Exception e) {
            mainApp.showMessage(Modality.NONE, "Error", e.toString());
        }
    }

    private void showNewKeuangan() {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailTransaksiKeuangan.fxml");
        DetailTransaksiKeuanganController x = loader.getController();
        x.setMainApp(mainApp, mainApp.MainStage, stage);
        x.setNewKeuangan();
        x.saveButton.setOnAction((ActionEvent event) -> {
            if ("0".equals(x.jumlahRpField.getText().replaceAll(",", ""))
                    || "".equals(x.jumlahRpField.getText().replaceAll(",", ""))) {
                mainApp.showMessage(Modality.NONE, "Warning", "Jumlah Rp masih kosong");
            } else if (x.kategoriCombo.getSelectionModel().getSelectedItem() == null) {
                mainApp.showMessage(Modality.NONE, "Warning", "Kategori belum dipilih");
            } else if (x.tipeKeuanganCombo.getSelectionModel().getSelectedItem() == null) {
                mainApp.showMessage(Modality.NONE, "Warning", "Tipe keuangan belum dipilih");
            } else {
                Task<String> task = new Task<String>() {
                    @Override
                    public String call() throws Exception {
                        try (Connection con = Koneksi.getConnection()) {
                            String tipe = "";
                            for (KategoriTransaksi k : allKategori) {
                                if (k.getKodeKategori().equals(x.kategoriCombo.getSelectionModel().getSelectedItem())) {
                                    tipe = k.getJenisTransaksi();
                                }
                            }
                            List<Keuangan> listKeuangan = new ArrayList<>();
                            if (Double.parseDouble(x.totalPropertyField.getText()) == 0) {
                                Keuangan k = new Keuangan();
                                k.setTglKeuangan(x.tglKeuanganPicker.getValue().toString());
                                k.setTipeKeuangan(x.tipeKeuanganCombo.getSelectionModel().getSelectedItem());
                                k.setKategori(x.kategoriCombo.getSelectionModel().getSelectedItem());
                                k.setKodeProperty("");
                                k.setDeskripsi(x.keteranganField.getText());
                                if (tipe.equals("D")) {
                                    k.setJumlahRp(Double.parseDouble(x.jumlahRpField.getText().replaceAll(",", "")));
                                } else {
                                    k.setJumlahRp(-Double.parseDouble(x.jumlahRpField.getText().replaceAll(",", "")));
                                }
                                k.setTotalImage(0);
                                k.setKodeUser(sistem.getUser().getUsername());
                                k.setTglInput(tglSql.format(new Date()));
                                k.setStatus("true");
                                k.setTglBatal("2000-01-01 00:00:00");
                                k.setUserBatal("");

                                Keuangan k2 = new Keuangan();
                                k2.setTglKeuangan(x.tglKeuanganPicker.getValue().toString());
                                k2.setKodeProperty("");
                                k2.setKategori(x.kategoriCombo.getSelectionModel().getSelectedItem());
                                k2.setDeskripsi(x.keteranganField.getText());
                                if (tipe.equals("D")) {
                                    k2.setTipeKeuangan("PENDAPATAN");
                                    k2.setJumlahRp(Double.parseDouble(x.jumlahRpField.getText().replaceAll(",", "")));
                                } else {
                                    k2.setTipeKeuangan("BEBAN");
                                    k2.setJumlahRp(Double.parseDouble(x.jumlahRpField.getText().replaceAll(",", "")));
                                }
                                k2.setTotalImage(0);
                                k2.setKodeUser(sistem.getUser().getUsername());
                                k2.setTglInput(tglSql.format(new Date()));
                                k2.setStatus("true");
                                k2.setTglBatal("2000-01-01 00:00:00");
                                k2.setUserBatal("");

                                listKeuangan.add(k);
                                listKeuangan.add(k2);
                            } else {
                                double total = Double.parseDouble(x.jumlahRpField.getText().replaceAll(",", ""));
                                double totalProp = 0;
                                double totalLuas = 0;
                                for (Keuangan d : x.allDetail) {
                                    if (d.isChecked()) {
                                        totalProp = totalProp + 1;
                                        totalLuas = totalLuas + d.getProperty().getLuasTanah();
                                    }
                                }
                                for (Keuangan d : x.allDetail) {
                                    if (d.isChecked()) {
                                        if (x.metode.equals("Rata-rata")) {
                                            d.setJumlahRp(total / totalProp);
                                        } else if (x.metode.equals("Luas Tanah")) {
                                            d.setJumlahRp(total * d.getProperty().getLuasTanah() / totalLuas);
                                        }
                                        Keuangan k = new Keuangan();
                                        k.setTglKeuangan(x.tglKeuanganPicker.getValue().toString());
                                        k.setKodeProperty(d.getKodeProperty());
                                        k.setKategori(x.kategoriCombo.getSelectionModel().getSelectedItem());
                                        k.setDeskripsi(x.keteranganField.getText());
                                        k.setTipeKeuangan(x.tipeKeuanganCombo.getSelectionModel().getSelectedItem());
                                        if (tipe.equals("D")) {
                                            k.setJumlahRp(d.getJumlahRp());
                                        } else {
                                            k.setJumlahRp(-d.getJumlahRp());
                                        }
                                        k.setTotalImage(0);
                                        k.setKodeUser(sistem.getUser().getUsername());
                                        k.setTglInput(tglSql.format(new Date()));
                                        k.setStatus("true");
                                        k.setTglBatal("2000-01-01 00:00:00");
                                        k.setUserBatal("");

                                        Keuangan k2 = new Keuangan();
                                        k2.setTglKeuangan(x.tglKeuanganPicker.getValue().toString());
                                        k2.setKodeProperty(d.getKodeProperty());
                                        k2.setKategori(x.kategoriCombo.getSelectionModel().getSelectedItem());
                                        k2.setDeskripsi(x.keteranganField.getText());
                                        if (tipe.equals("D")) {
                                            k2.setTipeKeuangan("PENDAPATAN");
                                            k2.setJumlahRp(d.getJumlahRp());
                                        } else {
                                            k2.setTipeKeuangan("BEBAN");
                                            k2.setJumlahRp(d.getJumlahRp());
                                        }
                                        k2.setTotalImage(0);
                                        k2.setKodeUser(sistem.getUser().getUsername());
                                        k2.setTglInput(tglSql.format(new Date()));
                                        k2.setStatus("true");
                                        k2.setTglBatal("2000-01-01 00:00:00");
                                        k2.setUserBatal("");

                                        listKeuangan.add(k);
                                        listKeuangan.add(k2);
                                    }
                                }
                            }
                            for(Keuangan k : listKeuangan){
                                k.setTotalImage(x.listImage.size());
                            }
                            if(x.totalImageField.getText().equals("0"))
                                return Service.saveAllTransaksiKeuangan(con, listKeuangan);
                            else{
                                return Service.saveAllTransaksiKeuanganWithImage(con, listKeuangan, x.listImage);
                            }
                        }
                    }
                };
                task.setOnRunning((e) -> {
                    mainApp.showLoadingScreen();
                });
                task.setOnSucceeded((WorkerStateEvent e) -> {
                    try {
                        mainApp.closeLoading();
                        if (task.getValue().equals("true")) {
                            mainApp.closeDialog(mainApp.MainStage, stage);
                            mainApp.showMessage(Modality.NONE, "Success", "Transaksi Keuangan berhasil disimpan");
                            getKeuangan();
                        } else {
                            mainApp.showMessage(Modality.NONE, "Error", task.getValue());
                        }
                    } catch (Exception ex) {
                        mainApp.showMessage(Modality.NONE, "Error", ex.toString());
                    }
                });
                task.setOnFailed((e) -> {
                    mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                    mainApp.closeLoading();
                });
                new Thread(task).start();
            }
        });
    }
    private void detailTransaksiKeuangan(Keuangan k) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailTransaksiKeuangan.fxml");
        DetailTransaksiKeuanganController x = loader.getController();
        x.setMainApp(mainApp, mainApp.MainStage, stage);
        x.setDetailKeuangan(k);
    }
    private void edit(Keuangan k) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/Edit.fxml");
        EditController x = loader.getController();
        x.setMainApp(mainApp, mainApp.MainStage, stage);
        x.textField.setText(k.getDeskripsi());
        x.saveButton.setOnAction((ActionEvent event) -> {
            Task<Void> task = new Task<Void>() {
                @Override
                public Void call() throws Exception {
                    try (Connection con = Koneksi.getConnection()) {
                        k.setDeskripsi(x.textField.getText());
                        KeuanganDAO.updateDeskripsi(con, k);
                    }
                    return null;
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((WorkerStateEvent e) -> {
                try {
                    mainApp.closeLoading();
                    mainApp.closeDialog(mainApp.MainStage, stage);
                    mainApp.showMessage(Modality.NONE, "Success", "Edit Keterangan berhasil disimpan");
                    getKeuangan();
                } catch (Exception ex) {
                    mainApp.showMessage(Modality.NONE, "Error", ex.toString());
                }
            });
            task.setOnFailed((e) -> {
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                mainApp.closeLoading();
            });
            new Thread(task).start();
        });
    }

    private void batal(Keuangan keu) {
        MessageController controller = mainApp.showMessage(Modality.WINDOW_MODAL, "Confirmation",
                "Batal transaksi keuangan " + keu.getNoKeuangan() + "-" + keu.getKategori() + " ?");
        controller.OK.setOnAction((ActionEvent ev) -> {
            Task<String> task = new Task<String>() {
                @Override
                public String call() throws Exception {
                    try (Connection con = Koneksi.getConnection()) {
                        return Service.batalTransaksiKeuangan(con, keu.getNoKeuangan());
                    }
                }
            };
            task.setOnRunning((e) -> {
                mainApp.showLoadingScreen();
            });
            task.setOnSucceeded((WorkerStateEvent e) -> {
                try {
                    mainApp.closeLoading();
                    if (task.getValue().equals("true")) {
                        mainApp.showMessage(Modality.NONE, "Success", "Batal transaksi berhasil disimpan");
                        getKeuangan();
                    } else {
                        mainApp.showMessage(Modality.NONE, "Error", task.getValue());
                    }
                } catch (Exception ex) {
                    mainApp.showMessage(Modality.NONE, "Error", ex.toString());
                }
            });
            task.setOnFailed((e) -> {
                mainApp.showMessage(Modality.NONE, "Error", task.getException().toString());
                mainApp.closeLoading();
            });
            new Thread(task).start();
        });
    }

    private void detailProperty(Property p) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailProperty.fxml");
        DetailPropertyController x = loader.getController();
        x.setMainApp(mainApp, mainApp.MainStage, stage);
        x.setProperty(p);
        x.viewOnly();
    }

    private void detailPembelian(Property p) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailPembelianTanah.fxml");
        DetailPembelianTanahController x = loader.getController();
        x.setMainApp(mainApp, mainApp.MainStage, stage);
        x.getPembelian(p);
    }

    private void detailPembangunan(String noPembangunan) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailPembangunan.fxml");
        DetailPembangunanController x = loader.getController();
        x.setMainApp(mainApp, mainApp.MainStage, stage);
        x.getPembangunan(noPembangunan);
    }

    private void detailRealisasi(String noRealisasi, String noUrut) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailRealisasiProyek.fxml");
        DetailRealisasiProyekController x = loader.getController();
        x.setMainApp(mainApp, mainApp.MainStage, stage);
        x.getDetailRealisasi(noRealisasi, noUrut);
    }

    private void detailTerimaPencairanKPR(Property p) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailTerimaPencairanKPR.fxml");
        DetailTerimaPencairanKPRController x = loader.getController();
        x.setMainApp(mainApp, mainApp.MainStage, stage);
        x.getKPR(p);
    }

    private void detailTerimaTandaJadi(String noSTJ) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailTerimaTandaJadi.fxml");
        DetailTerimaTandaJadiController x = loader.getController();
        x.setMainApp(mainApp, mainApp.MainStage, stage);
        x.getSTJ(noSTJ);
    }

    private void detailTerimaDownPayment(String noSDP) {
        Stage stage = new Stage();
        FXMLLoader loader = mainApp.showDialog(mainApp.MainStage, stage, "View/Dialog/DetailTerimaDownPayment.fxml");
        DetailTerimaDownPaymentController x = loader.getController();
        x.setMainApp(mainApp, mainApp.MainStage, stage);
        x.getSDP(noSDP);
    }

}
