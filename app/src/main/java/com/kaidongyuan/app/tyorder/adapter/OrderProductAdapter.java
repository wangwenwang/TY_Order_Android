package com.kaidongyuan.app.tyorder.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaidongyuan.app.tyorder.R;
import com.kaidongyuan.app.tyorder.bean.Product;
import com.kaidongyuan.app.tyorder.bean.ProductPolicy;
import com.kaidongyuan.app.tyorder.constants.BusinessConstants;
import com.kaidongyuan.app.tyorder.constants.URLCostant;
import com.kaidongyuan.app.tyorder.util.StringUtils;
import com.kaidongyuan.app.tyorder.widget.ScanfProductNumberDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/3.
 * 产品列表适配器
 */
public class OrderProductAdapter extends BaseExpandableListAdapter implements View.OnClickListener {

    private Context mContext;
    private List<Product> mProducts;
    private List<Product> mChoicedProducts;

    private final int HEAD_ITEM_TYPE=0;//头部item的样式常量
    private final int NORMAL_ITEM_TYPE=1;//正常产品item的样式常量
    /**
     * 回调接口
     */
    private OrderProductAdapterInterface mInterfae;
    /**
     * 回调搜索接口
     */
    private SearchProductInterface searchInterface;
    /**
     * 用户所选产品的IDX
     */
    private Long mInputCountToIndex;
    /**
     * 用户输入下单数量的 Dialog
     */
    private ScanfProductNumberDialog mInputDialog;

    public OrderProductAdapter(Context context, List<Product> products, List<Product> choicedProduct) {
        this.mContext = context;
        this.mProducts = products == null ? new ArrayList<Product>() : products;
        this.mChoicedProducts = choicedProduct == null ? new ArrayList<Product>() : choicedProduct;
    }

    public void notifyChange(List<Product> products, List<Product> choicedProduct) {
        this.mProducts = products == null ? new ArrayList<Product>() : products;
        this.mChoicedProducts = choicedProduct == null ? new ArrayList<Product>() : choicedProduct;
        this.notifyDataSetChanged();
    }
    public void notifyChoicedProductsChange(List<Product> choicedProduct){
        this.mChoicedProducts = choicedProduct == null ? new ArrayList<Product>() : choicedProduct;
        this.notifyDataSetChanged();
    }
    @Override
    public int getGroupType(int groupPosition) {
        if (groupPosition==0){
            return HEAD_ITEM_TYPE;
        }else {
            return NORMAL_ITEM_TYPE;
        }
    }
    @Override
    public int getGroupTypeCount() {
        return 2;
    }
    @Override
    public int getGroupCount() {
        //显示头部搜索栏目
        return mProducts.size()+1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<ProductPolicy> productPolicies = getPolicyList(mProducts.get(groupPosition-1).getPRODUCT_POLICY());
        return productPolicies.size();
    }

    @Override
    public Object getGroup(int groupPosition) {

            return mProducts.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (getGroupType(groupPosition)==NORMAL_ITEM_TYPE){
            GroupViewHolder groupViewHolder;
            if (convertView == null) {
                groupViewHolder = new GroupViewHolder();
                convertView = View.inflate(mContext, R.layout.item_product_list, null);
                groupViewHolder.textViewProductName = (TextView) convertView.findViewById(R.id.tv_productName);
                groupViewHolder.textViewProductStyle = (TextView) convertView.findViewById(R.id.tv_product_style);
                groupViewHolder.textViewCount = (TextView) convertView.findViewById(R.id.tv_count);
                groupViewHolder.textViewOriginPrice = (TextView) convertView.findViewById(R.id.tv_origin_price);
                groupViewHolder.imageViewProduct = (ImageView) convertView.findViewById(R.id.iv_product);
                groupViewHolder.imageViewDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
                groupViewHolder.imageViewAdd = (ImageView) convertView.findViewById(R.id.iv_add);
                groupViewHolder.imageViewArrowExpand = (ImageView) convertView.findViewById(R.id.iv_arrow_expand);
                groupViewHolder.linearLayoutPromotion = (LinearLayout) convertView.findViewById(R.id.ll_promotion);
                groupViewHolder.textViewProdcutInventory = (TextView) convertView.findViewById(R.id.tv_productInventory);
                groupViewHolder.textViewProductUom= (TextView) convertView.findViewById(R.id.tv_productUom);
                groupViewHolder.linearLayoutKucun = (LinearLayout) convertView.findViewById(R.id.ll_productInventory);
                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }


            //如果已选商品集合中有则用已选商品集合中的 Product 进行显示
            Product product = mProducts.get(groupPosition-1);
            int productIndexInChoicedProductList = getChoiceProductIndexInChoicedProductList(product);
            if (productIndexInChoicedProductList != -1) {
                product = mChoicedProducts.get(productIndexInChoicedProductList);
            }

            //显示已选商品数量
            int index = getChoiceProductIndexInChoicedProductList(product);
            if (index != -1) {
                groupViewHolder.textViewCount.setText(String.valueOf(product.getCHOICED_SIZE()));
            } else {
                groupViewHolder.textViewCount.setText(String.valueOf(0));
            }

            //设置是否显示促销信息栏
            if (getPolicyList(product.getPRODUCT_POLICY()).size() == 0) {
                groupViewHolder.linearLayoutPromotion.setVisibility(View.GONE);
            } else {
                groupViewHolder.linearLayoutPromotion.setVisibility(View.VISIBLE);
            }

            //设置显示促销信息图标
            if (isExpanded) {
                groupViewHolder.imageViewArrowExpand.setImageResource(R.drawable.button_drop_down);
            } else {
                groupViewHolder.imageViewArrowExpand.setImageResource(R.drawable.button_drop_up);
            }
            groupViewHolder.textViewProductName.setText(product.getPRODUCT_NAME().trim());
            groupViewHolder.textViewProductStyle.setText(product.getPRODUCT_DESC().trim());
            groupViewHolder.textViewOriginPrice.setText("￥" + product.getPRODUCT_PRICE());


            //库存是否显示
            if (BusinessConstants.ISNEED_CARE_NVENTORY.equals(product.getISINVENTORY())) {
                groupViewHolder.linearLayoutKucun.setVisibility(View.VISIBLE);
                groupViewHolder.textViewProdcutInventory.setText(String.valueOf(product.getPRODUCT_INVENTORY()));
                groupViewHolder.textViewProductUom.setText(product.getPRODUCT_UOM().trim());
            } else {
                groupViewHolder.linearLayoutKucun.setVisibility(View.VISIBLE);
                groupViewHolder.textViewProdcutInventory.setText(String.valueOf(product.getQTYAVAILABLE()));
                groupViewHolder.textViewProductUom.setText(product.getPRODUCT_UOM().trim());
            }

            //显示图片
            String productImgUrl = product.getPRODUCT_URL();
            if (TextUtils.isEmpty(productImgUrl)) {
                groupViewHolder.imageViewProduct.setImageResource(R.drawable.ic_gift);
            } else {
                productImgUrl = URLCostant.LOA_URL + productImgUrl;
                Picasso.with(mContext).load(productImgUrl).error(R.drawable.ic_gift).fit().into(groupViewHolder.imageViewProduct);
            }

            //设置监听
            groupViewHolder.imageViewAdd.setTag(product.getIDX());
            groupViewHolder.imageViewAdd.setOnClickListener(this);
            groupViewHolder.imageViewDelete.setTag(product.getIDX());
            groupViewHolder.imageViewDelete.setOnClickListener(this);
            groupViewHolder.textViewCount.setTag(product.getIDX());
            groupViewHolder.textViewCount.setOnClickListener(this);
        }else {
            SearchViewHolder searchViewHolder;
            if (convertView==null||convertView.getTag() instanceof GroupViewHolder){
                searchViewHolder=new SearchViewHolder();
                convertView=View.inflate(mContext,R.layout.item_search_productstyle,null);
                searchViewHolder.ed_product_style= (EditText) convertView.findViewById(R.id.ed_product_style);
                convertView.setTag(searchViewHolder);
            }else {
                searchViewHolder= (SearchViewHolder) convertView.getTag();
            }
            searchViewHolder.ed_product_style.addTextChangedListener(searchInterface);
            searchViewHolder.ed_product_style.setFocusable(true);
            searchViewHolder.ed_product_style.setFocusableInTouchMode(true);
            searchViewHolder.ed_product_style.requestFocus();
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            convertView = View.inflate(mContext, R.layout.item_promotion, null);
            childViewHolder.textViewChild = (TextView) convertView.findViewById(R.id.tv_state);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        ProductPolicy productPolicy = getPolicyList(mProducts.get(groupPosition).getPRODUCT_POLICY()).get(childPosition);
        childViewHolder.textViewChild.setText(productPolicy.getPOLICY_NAME());
        return convertView;
    }

    /**
     * 获取促销信息
     *
     * @param policies policies
     * @return 促销信息
     */
    private List<ProductPolicy> getPolicyList(List<ProductPolicy> policies) {
        List<ProductPolicy> policyList = new ArrayList<>();
        for (ProductPolicy productPolicy : policies) {
            if (!productPolicy.getPOLICY_TYPE().startsWith("1")) {
                policyList.add(productPolicy);
            }
        }
        return policyList;
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ImageView && mInterfae != null) {
            Long tag = (Long) v.getTag();
            switch (v.getId()) {
                case R.id.iv_add:
                    mInterfae.addProduct(tag);
                    break;
                case R.id.iv_delete:
                    mInterfae.deleteProduct(tag);
                    break;
            }
        } else if (v instanceof TextView && mInterfae != null) {
            switch (v.getId()) {
                case R.id.tv_count:
                    if (mInputDialog == null) {
                        showInputDialog();
                    }
                    mInputCountToIndex = (Long) v.getTag();
                    mInputDialog.show();
                    break;
            }
        }
    }

    /**
     * 显示输入 Dialog
     */
    private void showInputDialog() {
        if (mInputDialog == null) {
            mInputDialog = new ScanfProductNumberDialog(mContext);
        }
        mInputDialog.setInterface(new ScanfProductNumberDialog.ScanfProductNumberDialogInterface() {
            @Override
            public void pressConfrimButton(float inputNumber) {
                mInterfae.setProductCount(mInputCountToIndex, inputNumber);
            }
        });
        mInputDialog.show();
    }

    private class GroupViewHolder {
        TextView textViewProductName;
        TextView textViewProductStyle;
        TextView textViewCount;
        TextView textViewOriginPrice;
        ImageView imageViewProduct;
        ImageView imageViewDelete;
        ImageView imageViewAdd;
        ImageView imageViewArrowExpand;
        LinearLayout linearLayoutPromotion;
        LinearLayout linearLayoutKucun;
        TextView textViewProdcutInventory;//产品库存数目
        TextView textViewProductUom;//产品基本单位
    }

    private class ChildViewHolder {
        TextView textViewChild;
    }
    private class SearchViewHolder{
        EditText ed_product_style;
    }

    /**
     * 关键字搜索产品列表 的回调接口
     */
    public interface SearchProductInterface extends TextWatcher {}

    public void setSearchInterface(SearchProductInterface searchInterface) {
        this.searchInterface = searchInterface;
    }

    /**
     * Created by Administrator on 2016/4/21.
     * 添加赠品 Adapter 的回调接口
     */
    public interface OrderProductAdapterInterface {

        /**
         * 单个增加商品
         *
         * @param dataIndex 产品列表中的位置
         */
        void addProduct(Long dataIndex);

        /**
         * 单个减少商品
         *
         * @param dataIndex 产品列表中的位置
         */
        void deleteProduct(Long dataIndex);

        /**
         * 直接设置赠品的商品
         *
         * @param dataIndex 产品列表中的位置
         * @param giftCount 赠品数量
         */
        void setProductCount(Long dataIndex, float giftCount);

    }

    /**
     * 设置接口
     *
     * @param orderProductAdapterInterface 回调接口
     */
    public void setInterface(OrderProductAdapterInterface orderProductAdapterInterface) {
        this.mInterfae = orderProductAdapterInterface;
    }

    /**
     * 获取产品在已选产品集合中的位置
     *
     * @param choiceProduct 需要查询的商品
     * @return 在选择集合中的位置，没有则返回 -1
     */
    private int getChoiceProductIndexInChoicedProductList(Product choiceProduct) {
        try {
            int index = -1;
            int size = mChoicedProducts.size();
            Product productInChoicedList;
            for (int i = 0; i < size; i++) {//根据产品的 IDX 去查找产品在集合中的位置
                productInChoicedList = mChoicedProducts.get(i);
                if (choiceProduct.getIDX() == productInChoicedList.getIDX()) {
                    index = i;
                }
            }
            return index;
        } catch (Exception e) {
            return -1;
        }
    }
}










