package com.shop.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shop.ecommerce.dto.ImageHolder;
import com.shop.ecommerce.dto.ShopDto;
import com.shop.ecommerce.entity.Area;
import com.shop.ecommerce.entity.Shop;
import com.shop.ecommerce.entity.ShopCategory;
import com.shop.ecommerce.entity.User;
import com.shop.ecommerce.enums.ShopState;
import com.shop.ecommerce.exceptions.ShopOperationException;
import com.shop.ecommerce.service.AreaService;
import com.shop.ecommerce.service.ShopCategoryService;
import com.shop.ecommerce.service.ShopService;

import com.shop.ecommerce.utils.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shopadmin")
public class ShopController {

    @Autowired
    ShopService shopService;

    @Autowired
    private ShopCategoryService shopCategoryService;

    @Autowired
    private AreaService areaService;


    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<Area>();
        try {
            shopCategoryList = shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
            modelMap.put("shopCategoryList", shopCategoryList);
            modelMap.put("areaList", areaList);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }

    @RequestMapping(value = "/registershop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> registerShop(@RequestParam(value = "shop") String shopStr, @RequestParam(value = "shopImg") MultipartFile shopImg) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // map shopStr to shop
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }


        // register shop
        if (shop != null && shopImg != null) {
            try {
                ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                ShopDto shopDto = shopService.addShop(shop,imageHolder);
                if (shopDto.getState() == ShopState.CHECK.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopDto.getStateInfo());
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "can't insert empty shop");
            return modelMap;
        }
    }

    @RequestMapping(value = "/getshopbyid/{shopId}", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopById(@PathVariable Long shopId) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        if (shopId > -1) {
            try {
                Shop shop = shopService.getByShopId(shopId);
                List<Area> areaList = areaService.getAreaList();
                modelMap.put("shop", shop);
                modelMap.put("areaList", areaList);
                modelMap.put("success", true);
            } catch (Exception e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyshop", method = RequestMethod.POST)
    @ResponseBody
    private Map<String, Object> modifyShop(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // 1.get and transform all input data
        String shopStr = HttpServletRequestUtil.getString(request, "shopStr");
        ObjectMapper mapper = new ObjectMapper();
        Shop shop = null;
        try {
            shop = mapper.readValue(shopStr, Shop.class);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (commonsMultipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            shopImg = (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        // 2.modify shop info
        if (shop != null && shop.getShopId() != null) {
            ShopDto shopDto;
            User owner = new User();
            owner.setUserId(1L);
            shop.setOwner(owner);
            try {
                if (shopImg == null) {
                    shopDto = shopService.modifyShop(shop, null);
                } else {
                    ImageHolder imageHolder = new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                    shopDto = shopService.modifyShop(shop, imageHolder);
                }
                if (shopDto.getState() == ShopState.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", shopDto.getStateInfo());
                }
            } catch (ShopOperationException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            } catch (IOException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.getMessage());
            }
            return modelMap;
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "empty shopId");
            return modelMap;
        }
    }

    @RequestMapping(value = "/getshoplist", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopList(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        // TODO: remve these 3 line when login/session code is complete
        User owner = new User();
        owner.setUserId(1L);
        request.getSession().setAttribute("user", owner);
        //get owner from session
        owner = (User) request.getSession().getAttribute("user");
        try {
            Shop shopCondition = new Shop();
            shopCondition.setOwner(owner);
            ShopDto shopDto = shopService.getShopList(shopCondition, 0, 100);
            modelMap.put("shopList", shopDto.getShopList());
            // put shoplist into session so that it will be accessible by this user in the session
            request.getSession().setAttribute("shopList", shopDto.getShopList());
            modelMap.put("user", owner);
            modelMap.put("success", true);
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.getMessage());
        }
        return modelMap;
    }
}