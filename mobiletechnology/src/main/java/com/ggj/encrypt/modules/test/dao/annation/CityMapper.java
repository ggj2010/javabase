/**
 *    Copyright 2015-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.ggj.encrypt.modules.test.dao.annation;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.ggj.encrypt.modules.test.bean.City;

/**
 * @author:gaoguangjin
 * @date 2016/5/30 16:33
 */
@Mapper
public interface CityMapper {
	@Select("select * from city where id = #{id}")
	City getCity(@Param("id") int id);

	@Insert("INSERT into City (name,country)VALUE (#{name},#{country})")
	void insert(City city);
}
