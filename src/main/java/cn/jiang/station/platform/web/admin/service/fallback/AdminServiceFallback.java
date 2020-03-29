package cn.jiang.station.platform.web.admin.service.fallback;

import cn.jiang.station.platform.common.constants.HttpStatusConstants;
import cn.jiang.station.platform.common.dto.BaseResult;
import cn.jiang.station.platform.common.utils.MapperUtils;
import cn.jiang.station.platform.web.admin.service.AdminService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

@Component
public class AdminServiceFallback implements AdminService {
    @Override
    public String login(String loginCode, String password) {
        BaseResult baseResult = BaseResult.notOk(Lists.newArrayList(new BaseResult.Error(String.valueOf(HttpStatusConstants.BAD_GATEWAY.getStatus()), HttpStatusConstants.BAD_GATEWAY.getContent())));
        try {
            return MapperUtils.obj2json(baseResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
