package com.djn.order.rule;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 自定义负载均衡策略：每3次调用后轮换
 */
public class CustomLoadBalancer implements ReactorServiceInstanceLoadBalancer {

    private static final Log log = LogFactory.getLog(CustomLoadBalancer.class);

    /**
     * 被调用的次数
     */
    private int total = 0;

    /**
     * 当前提供服务的实例的索引
     */
    private int index = 0;

    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider;

    private String serviceId;

    public CustomLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider,
                              String serviceId) {
        this.serviceInstanceListSupplierObjectProvider = serviceInstanceListSupplierObjectProvider;
        this.serviceId = serviceId;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierObjectProvider.getIfAvailable();
        assert supplier != null;
        return supplier.get().next().map(this::getInstanceResponse);
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        log.info("进入自定义负载均衡");

        if(instances.isEmpty()) {
            return new EmptyResponse();
        }

        log.info("每个服务访问3次后轮换");
        int size = instances.size();

        ServiceInstance serviceInstance = null;

        while (serviceInstance == null) {
            System.out.println("======" + total);
            System.out.println("======" + index);
            if (total < 3) {
                serviceInstance = instances.get(index);
                total++;
            } else {
                total = 0;
                index ++;
                if (index >= size) {
                    index = 0;
                }
                serviceInstance = instances.get(index);
            }
        }

        return new DefaultResponse(serviceInstance);
    }
}
