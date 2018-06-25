/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.AutorDAO;
import dao.CarteDAO;
import dao.CarteHasAutorDAO;
import dao.ClientDAO;
import dao.ComandaDAO;
import dao.ComandaHasCarteDAO;
import dao.EdituraDAO;
import dao.FurnizorDAO;
import dao.exceptions.IllegalOrphanException;
import dao.exceptions.NonexistentEntityException;
import db.AutorDB;
import db.CarteDB;
import db.CarteHasAutorDB;
import db.ClientDB;
import db.ComandaDB;
import db.ComandaHasCarteDB;
import db.EdituraDB;
import db.FurnizorDB;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Ionut
 */
public class MainController {
    private static MainController singleton;
    
    private EntityManagerFactory emf;
    private AutorDAO autorDao;
    private CarteDAO carteDao;
    private EdituraDAO edituraDao;
    private FurnizorDAO furnizorDao;
    private ComandaDAO comandaDao;
    private ClientDAO clientDao;
    private CarteHasAutorDAO cartehasautorDao;
    private ComandaHasCarteDAO comandahascarteDao;
    
    private MainController(){
        emf = Persistence.createEntityManagerFactory("ProiectBDPU");
        autorDao = new AutorDAO(emf);
        carteDao = new CarteDAO(emf);
        edituraDao = new EdituraDAO(emf);
        furnizorDao = new FurnizorDAO(emf);
        comandaDao = new ComandaDAO(emf);
        clientDao = new ClientDAO(emf);
        cartehasautorDao = new CarteHasAutorDAO(emf);
        comandahascarteDao = new ComandaHasCarteDAO(emf);  
    }
    
    public static MainController getInstance(){
        if(singleton == null){
            singleton = new MainController();
        }
        return singleton;
    }
    
    public boolean adaugaAutor(String nume, String prenume, String cnp){
        AutorDB autor = autorDao.findAutorByCNP(cnp);
        if(autor == null){
            autor = new AutorDB();
            autor.setNume(nume);
            autor.setPrenume(prenume);
            autor.setCnp(cnp);
            autorDao.create(autor);
            return true;
        }
        return false;
    }
    
    public List<AutorDB> getAutori(){
        return autorDao.findAutorDBEntities();
    }
    
    public void stergeAutor(AutorDB a){
        try {
            autorDao.destroy(a.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<AutorDB> getAutoriByCarte(CarteDB carte){
        return autorDao.findAutoriByCarte(carte);
    }
    
    public EdituraDB findEdituraByNume(String nume){
        return edituraDao.findEdituraByNume(nume);
    }
    
    public boolean adaugaEditura(String nume, String oras){
        EdituraDB editura = edituraDao.findEdituraByNume(nume);
        
        if(editura == null){
            editura = new EdituraDB();
            editura.setNume(nume);
            editura.setOras(oras);
            edituraDao.create(editura);
            return true;
        }
        return false;
    }
    
    public List<EdituraDB> getEdituri(){
        return edituraDao.findEdituraDBEntities();
    }
    
    public void stergeEditura(EdituraDB ed){
        try{
            List<CarteDB> carti = carteDao.findCartiByEditura(ed);
            for(CarteDB c: carti){
                stergeCarte(c);
            }
            edituraDao.destroy(ed.getId());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<EdituraDB> getEdituriByComenzi(){
        return edituraDao.findEdituriByComenzi();
    }
    
    
    public boolean adaugaFurnizor(String nume, String tel){
        FurnizorDB furnizor = furnizorDao.findFurnizorByNume(nume);
        
        if(furnizor == null){
            furnizor = new FurnizorDB();
            furnizor.setNume(nume);
            furnizor.setTel(tel);
            furnizorDao.create(furnizor);
            return true;
        }
        return false;
    }
    
    public List<FurnizorDB> getFurnizori(){
        return furnizorDao.findFurnizorDBEntities();
    }
    
    public void stergeFurnizor(FurnizorDB f){
        try{
            List<CarteDB> carti = carteDao.findCartiByFurnizor(f);
            for(CarteDB c: carti){
                stergeCarte(c);
            }
            furnizorDao.destroy(f.getId());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<FurnizorDB> getFurnizoriByComenzi(){
        return furnizorDao.findFurnizoriByComenzi();
    }
      
    public boolean adaugaCarte(String titlu, String isbn, int stoc, double pret, EdituraDB editura, FurnizorDB furnizor, ArrayList<AutorDB> autori){
        CarteDB carte = carteDao.findCarteByISBN(isbn);
        
        if(carte == null){
            carte = new CarteDB();
            carte.setTitlu(titlu);
            carte.setIsbn(isbn);
            carte.setPret(pret);
            carte.setStoc(stoc);
            carte.setEditura(editura);
            carte.setFurnizor(furnizor);
            carteDao.create(carte);
            for(AutorDB a:autori){
                CarteHasAutorDB cartehasautorDB = new CarteHasAutorDB();
                cartehasautorDB.setAutor(a);
                cartehasautorDB.setCarte(carteDao.findCarteByISBN(isbn));
                cartehasautorDao.create(cartehasautorDB);
            }
            
            return true;
        }
        return false;
    }
    
    public List<CarteDB> getCarti(){
        return carteDao.findCarteDBEntities();
    }
    
    
    
    public void stergeCarte(CarteDB carte){
        try {
            List<CarteHasAutorDB> cha = cartehasautorDao.findChaByCarte(carte);
            for(CarteHasAutorDB c: cha){
                cartehasautorDao.destroy(c.getId()); 
            }
            
            List<ComandaHasCarteDB> chc = comandahascarteDao.findChcByCarte(carte);
            for(ComandaHasCarteDB d: chc){
                comandahascarteDao.destroy(d.getId());
            }
                    
            carteDao.destroy(carte.getId());        
        } catch (IllegalOrphanException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<CarteDB> getCartiByComanda(ComandaDB comanda){
        return carteDao.findCartiByComanda(comanda);
    }
    
    public List<CarteDB> getCartiByAutor(AutorDB autor){
        return carteDao.findCartiByAutor(autor);
    }
    
    public void modificaCarte(CarteDB carte){
        try {
            carteDao.edit(carte);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<ClientDB> getClienti(){
        return clientDao.findClientDBEntities();
    }
    
    public boolean adaugareClient(String nume, String prenume, String cnp, String oras, String strada, int nr, String tel, String email){
        ClientDB client = clientDao.findClientByCNP(cnp);
        
        if(client == null){
            client = new ClientDB();
            client.setNume(nume);
            client.setPrenume(prenume);
            client.setCnp(cnp);
            client.setOras(oras);
            client.setStrada(strada);
            client.setNr(nr);
            client.setTel(tel);
            client.setEmail(email);
            clientDao.create(client);
            return true;
        }
        return false;
    }
    
    public void stergeClient(ClientDB client){
        try {
            clientDao.destroy(client.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public List<ComandaDB> getComenziByClient(ClientDB client){
        return comandaDao.findComenziByClient(client);
    }
    
    public void adaugaComanda(ClientDB client){
        ComandaDB comanda = new ComandaDB();
        comanda.setStatus("IN DESFASURARE");
        
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        comanda.setDate(dt);
        comanda.setClient(client);
        comandaDao.create(comanda);
    }
    
    public void adaugaCarteComanda(CarteDB carte, ComandaDB comanda, int cantitate){
        ComandaHasCarteDB cha = new ComandaHasCarteDB();
        cha.setComanda(comanda);
        cha.setCarte(carte);
        cha.setCantitate(cantitate);
        comandahascarteDao.create(cha);
    }
    
    public int getCantitate(ComandaDB comanda, CarteDB carte){
        return comandahascarteDao.findChaByComandaAndCarte(comanda, carte);
    }
    
    public Integer getCantitateTotala(CarteDB carte){
        return carteDao.findCantitateTotala(carte);
    }
    
    public double getTotal(ComandaDB comanda){
        return comandaDao.findTotalForComanda(comanda);
    }
    
    public void modificaComanda(ComandaDB comanda){
        try {
            comandaDao.edit(comanda);
        } catch (Exception ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
