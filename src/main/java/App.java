import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {
        //TODO: write code here

    	List<Item> items = this.itemRepository.findAll();        //itemList
        HashMap<String,Item> inputItemsMap = getInputItem(items,inputs);      //inputItemsMap
        int totalPrice = 0 ;
        String orderDetails = "============= Order details =============\n";           //orderDetails
        for(String str : inputs){
            String s = str.charAt(str.length()-1) +"";
            int num = Integer.parseInt(s);
            String id = str.substring(0,8);
            for(Item item : items){
                if(id.equals( item.getId())){
                    orderDetails += item.getName() + " x " ;
                    orderDetails += num + " = " + Math.round(num *item.getPrice()) + " yuan\n";
                    totalPrice += num *item.getPrice();
                }
            }
        }
        orderDetails +=  "-----------------------------------\n";
        String promotionStr = "";
        List<SalesPromotion> salesPromotions = this.salesPromotionRepository.findAll();   //salesPromotionList
        String proItem = getHalfItemNames(salesPromotions,inputItemsMap,inputs);
        int halfPriceLess = judgeHalfPrice(salesPromotions,inputItemsMap,inputs);
        int lessMoney = (totalPrice - halfPriceLess);
        if(totalPrice >= 30){
            if(halfPriceLess > 6){
                    promotionStr= "Promotion used:\n" +
                            "Half price for certain dishes ("+proItem+")，saving "+halfPriceLess+" yuan\n" +
                            "-----------------------------------\n" +
                            "Total："+ lessMoney + " yuan\n" +
                            "===================================";
                return orderDetails + promotionStr;
            }else{
                promotionStr +="Promotion used:\n" +"满30减6 yuan，saving 6 yuan\n" +
                        "-----------------------------------\n" +
                        "Total：" +(totalPrice -6) +" yuan\n" +
                        "===================================";
                return orderDetails + promotionStr;
            }
        }else if(halfPriceLess > 0){
            promotionStr += "Promotion used:\n"+"Half price for certain dishes (" + proItem + ")，saving " + halfPriceLess + " yuan\\n" +"-----------------------------------\n" +
                    "Total：" + (totalPrice - halfPriceLess) + "yuan\n" +
                    "===================================";
            return orderDetails + promotionStr;
        }else {
            promotionStr +=  "Total："+ totalPrice+" yuan\n" +
                    "===================================";
            return orderDetails + promotionStr;
        }
    }
    public int judgeHalfPrice(List<SalesPromotion> salesPromotions,HashMap<String,Item> inputItemsMap,List<String> inputs){
        boolean flag = true;
        int halfPriceRest = 0;
        for(int i = 0 ; i < salesPromotions.size(); i ++){
            List<String> relatedList = salesPromotions.get(i).getRelatedItems();
            if(relatedList.size() > 0){
                for(String str :relatedList){
                    if(inputItemsMap.get(str) == null){
                        flag = false;
                    }
                }
                if(flag){
                    HashMap<String,Integer> amountMap = getInputItemAmount(inputs);
                List<String> relatedList2 = salesPromotions.get(i).getRelatedItems();
                for(String str :relatedList){
                    Item item = inputItemsMap.get(str);
                    halfPriceRest += item.getPrice() * amountMap.get(str);
                }
                return halfPriceRest / 2;
                }
            }
        }
        return 0;
    }
    public String getHalfItemNames(List<SalesPromotion> salesPromotions,HashMap<String,Item> inputItemsMap,List<String> inputs){
        boolean flag = true;
        String retStr = "";
        for(int i = 0 ; i < salesPromotions.size(); i ++){
            List<String> relatedList = salesPromotions.get(i).getRelatedItems();
            if(relatedList.size() > 0){
                for(String str :relatedList){
                    if(inputItemsMap.get(str) == null){
                        flag = false;
                    }
                }
                if(flag){
                    for(String str :relatedList){
                        retStr += inputItemsMap.get(str).getName() + "，";
                    }
                    return retStr.substring(0,retStr.length()-1);
                }
            }
        }
        return null;
    }
    public HashMap<String,Item> getInputItem(List<Item> items, List<String> inputs){
        HashMap<String,Item> inputItem = new HashMap<String,Item>();
        for(String str : inputs){
            String id = str.substring(0,8);
            for(Item item : items){
                if(id.equals( item.getId())){
                    inputItem.put(id,item);
                }
            }
        }
        return inputItem;
    }
    public HashMap<String,Integer> getInputItemAmount(List<String> inputs){
        HashMap<String,Integer> inputItemAmount = new HashMap<String,Integer>();
        for(String str : inputs){
            String s = str.charAt(str.length()-1) +"";
            int num = Integer.parseInt(s);
            String id = str.substring(0,8);
            inputItemAmount.put(id,num);
        }
        return inputItemAmount;
    }
}
