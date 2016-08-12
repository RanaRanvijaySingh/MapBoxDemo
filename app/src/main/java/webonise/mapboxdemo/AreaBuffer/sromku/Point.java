package webonise.mapboxdemo.areabuffer.sromku;
/**
 * Point on 2D landscape
 * Copyright 2013-present Roman Kushnarenko
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Roman Kushnarenko (sromku@gmail.com)
 * @Original-Git-repo-link : https://github.com/sromku/polygon-contains-point
 */
public class Point
{
    public Point(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double x;
    public double y;

    @Override
    public String toString()
    {
        return String.format("(%.2f,%.2f)", x, y);
    }
}