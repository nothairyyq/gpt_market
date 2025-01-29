package com.priska.domain.activity.service;

import com.priska.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

/**
 * @program: IntelliJ IDEA
 * @description:
 * @author: Priska
 * @create: 2025-01-29
 */
@Service
public class RaffleActivityService extends AbstractRaffleActivity {

    public RaffleActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }
}
