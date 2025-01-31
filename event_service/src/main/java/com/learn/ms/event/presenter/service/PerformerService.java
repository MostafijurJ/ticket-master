package com.learn.ms.event.presenter.service;

import com.learn.ms.event.core.domain.request.PerformerRequest;
import com.learn.ms.event.core.domain.response.PerformerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerformerService {
    PerformerResponse createPerformer(PerformerRequest performerRequest);

    Page<PerformerResponse> getAllPerformers(Pageable pageable);

    PerformerResponse getPerformerById(Long id);

    PerformerResponse updatePerformer(Long id, PerformerRequest performer);

    PerformerResponse deletePerformer(Long id);

    Boolean performerExists(String name);
}
