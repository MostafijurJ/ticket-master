package com.learn.ms.event.core.service;

import com.learn.ms.event.common.mapper.PerformerMapper;
import com.learn.ms.event.core.domain.enums.ResponseMessage;
import com.learn.ms.event.core.domain.exceptions.InvalidRequestDataException;
import com.learn.ms.event.core.domain.request.PerformerRequest;
import com.learn.ms.event.core.domain.response.PerformerResponse;
import com.learn.ms.event.data.entity.Performer;
import com.learn.ms.event.data.repository.PerformerRepository;
import com.learn.ms.event.presenter.service.PerformerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformerServiceImpl extends BaseService implements PerformerService {
    private final PerformerRepository performerRepository;
    private final PerformerMapper performerMapper;

    @Override
    public PerformerResponse createPerformer(PerformerRequest performerRequest) {
        Performer byName = performerRepository.findByNameIgnoreCase(performerRequest.getName());
        if (!ObjectUtils.isEmpty(byName)) {
            throw new InvalidRequestDataException(ResponseMessage.PERFORMER_ALREADY_EXISTS);
        }

        Performer performer = performerMapper.mapRequestToEntity(performerRequest);
        Performer savedPerformer = performerRepository.save(performer);
        return performerMapper.mapToResponse(savedPerformer);
    }

    @Override
    public Page<PerformerResponse> getAllPerformers(Pageable pageable) {
        return performerRepository.findAll(pageable).map(performerMapper::mapToResponse);
    }

    @Override
    public PerformerResponse getPerformerById(Long id) {
        Performer performer = performerRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.PERFORMER_NOT_FOUND));
        return performerMapper.mapToResponse(performer);
    }

    @Override
    public PerformerResponse updatePerformer(Long id, PerformerRequest performerRequest) {
        Performer performer = performerRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.PERFORMER_NOT_FOUND));
        performer.setName(performerRequest.getName());
        Performer updatedPerformer = performerRepository.save(performer);
        return performerMapper.mapToResponse(updatedPerformer);
    }

    @Override
    public PerformerResponse deletePerformer(Long id) {
        Performer performer = performerRepository.findById(id)
                .orElseThrow(() -> new InvalidRequestDataException(ResponseMessage.PERFORMER_NOT_FOUND));
        performerRepository.delete(performer);
        return performerMapper.mapToResponse(performer);
    }

    @Override
    public Boolean performerExists(String name) {
        Performer byName = performerRepository.findByNameIgnoreCase(name);
        if (!ObjectUtils.isEmpty(byName)) {
            throw new InvalidRequestDataException(ResponseMessage.PERFORMER_ALREADY_EXISTS);
        }
        return false;
    }
}
