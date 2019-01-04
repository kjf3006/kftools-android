package com.appverlag.kf.kftools.weather;

import org.json.JSONObject;

/**
 * Copyright (C) Kevin Flachsmann - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Created by kevinflachsmann on 27.12.18.
 */
public interface KFWeatherParserProtocol {
    KFWeatherForecast parseResponse(JSONObject response);
}
