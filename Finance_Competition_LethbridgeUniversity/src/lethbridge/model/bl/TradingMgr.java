package lethbridge.model.bl;


import lethbridge.model.entities.TradingEntity;
import lethbridge.model.entities.UserEntity;
import lethbridge.model.pattern.CDIBaseModel;
import util.R;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Named
@RequestScoped
public class TradingMgr extends CDIBaseModel implements Serializable {

    @Inject
    private UsersMgr usersMgr;

    @Transactional
    public void registerRequest(TradingEntity tradingEntity) throws Exception {

        final double currentInvestment =
                usersMgr.getCurrentInvestment(tradingEntity.getUserID());
        switch (tradingEntity.getTradingType()) {
            case "buy": {
                long numberOfUnit = tradingEntity.getNumberOfUnit();
                double lastPrice = tradingEntity.getLastPrice();
                double cost = (numberOfUnit * lastPrice * 1.0d);
                if (cost <= currentInvestment) {
                    tradingEntity.setStatus(R.PublicText.FILLED);
                    entityManager.persist(tradingEntity);
                    usersMgr.editCurrentInvestment(tradingEntity.getUserID(),
                            currentInvestment - (cost * 1.0d));
                } else {
                    tradingEntity.setStatus(R.PublicText.CANCELED);
                    entityManager.persist(tradingEntity);
                    throw new Exception(R.Messages.Error.PURCHASE_DENIED);
                }
                break;
            }
            case "sell": {
                long numberOfStock = getNumberOfStocks(tradingEntity.getUserID(),
                        tradingEntity.getCompanyName());
                double totalPrice = tradingEntity.getNumberOfUnit() * tradingEntity.getLastPrice() * 1.0d;

                if (tradingEntity.getNumberOfUnit() <= numberOfStock) {
                    tradingEntity.setStatus(R.PublicText.FILLED);
                    entityManager.persist(tradingEntity);
                    usersMgr.editCurrentInvestment(tradingEntity.getUserID(),
                            currentInvestment + (totalPrice * 1.d));
                } else {
                    tradingEntity.setStatus(R.PublicText.CANCELED);
                    entityManager.persist(tradingEntity);
                    throw new Exception(R.Messages.Error.SELL_DENIED);
                }
            }
        }
    }

    public long getNumberOfStocks(String userID, String companyName) throws Exception {
        Long sellNo = (Long) entityManager.createQuery("select sum(te.numberOfUnit) from TradingEntity te " +
                "where te.userID = :id and te.companyName = :cmpName and te.tradingType = :tt " +
                "and te.status = :st")
                .setParameter("id", userID)
                .setParameter("cmpName", companyName)
                .setParameter("tt", R.PublicText.SELL)
                .setParameter("st", R.PublicText.FILLED).getSingleResult();

        Long buyNo = (Long) entityManager.createQuery("select sum(te.numberOfUnit) from TradingEntity te " +
                "where te.userID = :id and te.companyName = :cmpName and te.tradingType = :tt " +
                "and te.status = :st")
                .setParameter("id", userID)
                .setParameter("cmpName", companyName)
                .setParameter("tt", R.PublicText.BUY)
                .setParameter("st", R.PublicText.FILLED).getSingleResult();
        if (sellNo == null) {
            sellNo = 0L;
        }
        if (buyNo == null) {
            buyNo = 0L;
        }
        return buyNo - sellNo;
    }

    @Transactional
    public void registerCancelRequest(TradingEntity tradingEntity) throws Exception {
        tradingEntity.setStatus(R.PublicText.CANCELED);
        entityManager.persist(tradingEntity);
    }

    @Transactional
    public List<TradingEntity> getTradingItems(UserEntity userEntity) throws Exception {
        return entityManager.createQuery("select te from TradingEntity te where te.userID = :id",
                TradingEntity.class).setParameter("id", userEntity.getUserID()).getResultList();
    }

    @Transactional
    public List<String> getTradingCompanyNames(UserEntity userEntity) throws Exception {
        return entityManager.createQuery("select distinct te.companyName from TradingEntity te where te.userID = :id",
                String.class).setParameter("id", userEntity.getUserID()).getResultList();
    }

    @Transactional
    public void removeTradingInfo(String userID) throws Exception {
        entityManager.createQuery("delete from TradingEntity te where trim(te.userID) = :userID")
                .setParameter("userID", userID).executeUpdate();
    }
}
