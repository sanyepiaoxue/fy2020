package com.neuedu.dao;

import com.neuedu.pojo.Product;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProductMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    int insert(Product record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    Product selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    List<Product> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table neuedu_product
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Product record);


    //更新商品信息，只更新非空字段
    int updateProductByActivate(@Param("product") Product product);

    //按照商品名称和categoryId做检索
    List<Product> findProducsByCategoryIdsAndkeyword(@Param("categoryId") List<Integer> ids,
                                                     @Param("keyword") String keyword);
}