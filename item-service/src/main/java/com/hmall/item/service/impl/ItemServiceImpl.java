package com.hmall.item.service.impl;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmall.common.exception.BizIllegalException;
import com.hmall.common.utils.BeanUtils;
import com.hmall.item.domain.dto.ItemDTO;
import com.hmall.item.domain.dto.OrderDetailDTO;
import com.hmall.item.domain.po.Item;
import com.hmall.item.domain.query.ItemPageQueryDTO;
import com.hmall.item.mapper.ItemMapper;
import com.hmall.item.service.IItemService;
import com.hmall.item.service.ItemRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.List;


/**
 * <p>
 * 商品表 服务实现类
 * </p>
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements IItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ItemRedisService itemRedisService;

    /**
     * 分页查询
     *
     * @param itemPageQueryDTO
     * @return
     */
    @Override
    public Page<Item> pageQuery(ItemPageQueryDTO itemPageQueryDTO) {
        Page<Item> itemPage = itemMapper.selectPage(
                new Page<>(itemPageQueryDTO.getPageNo(), itemPageQueryDTO.getPageSize()),
                new LambdaQueryWrapper<Item>().like(StrUtil.isNotBlank(itemPageQueryDTO.getKey()), Item::getName, itemPageQueryDTO.getKey())
                        .eq(StrUtil.isNotBlank(itemPageQueryDTO.getBrand()), Item::getBrand, itemPageQueryDTO.getBrand())
                        .eq(StrUtil.isNotBlank(itemPageQueryDTO.getCategory()), Item::getCategory, itemPageQueryDTO.getCategory())
                        .eq(Item::getStatus, 1)
                        .between(itemPageQueryDTO.getMaxPrice() != null, Item::getPrice, itemPageQueryDTO.getMinPrice(), itemPageQueryDTO.getMaxPrice())
        );
        return itemPage;
    }

    @Override
    public void deductStock(List<OrderDetailDTO> items) {
        String sqlStatement = "com.hmall.item.mapper.ItemMapper.updateStock";
        boolean r = false;
        try {
            r = executeBatch(items, (sqlSession, entity) -> sqlSession.update(sqlStatement, entity));
        } catch (Exception e) {
            throw new BizIllegalException("更新库存异常，可能是库存不足!", e);
        }
        if (!r) {
            throw new BizIllegalException("库存不足！");
        }
    }

    @Override
    public List<ItemDTO> queryItemByIds(Collection<Long> ids) {
        List<ItemDTO> itemListCache = itemRedisService.getItemListCache(ids);
        if (!ObjectUtils.isEmpty(itemListCache)) {
            return itemListCache;
        }
        List<ItemDTO> itemDTO = BeanUtils.copyList(listByIds(ids), ItemDTO.class);
        itemRedisService.saveItemListCache(itemDTO);
        return itemDTO;
    }
}
