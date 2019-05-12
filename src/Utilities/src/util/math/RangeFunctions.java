package util.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import util.math.RangeFunctions.Day.DayName;

public final class RangeFunctions {

    public static class RangeObject {

        private final double min, max;
        public static final RangeObject EMPTY_SET = new RangeObject(0, -1) {
            @Override
            public String toString() {
                return "[]";
            }
        };

        public RangeObject(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public static boolean collide(RangeObject r1, RangeObject r2) {
            if (r1.min < r2.min && r2.min < r1.max) {
                return true;
            }
            if (r2.min < r1.min && r1.min < r2.max) {
                return true;
            }
            if (r1.min == r2.min && r1.max == r2.max) {
                return true;
            }
            return false;
        }

        public boolean collidesWith(RangeObject r) {
            if (this.equals(EMPTY_SET) || r.equals(EMPTY_SET)) {
                System.out.println("EMPTY");
                return false;
            }
            return collide(this, r);
        }

        public RangeObject intersect(RangeObject r) {
            if (collide(this, r) == false) {
                return EMPTY_SET;
            }
            RangeObject retObject = new RangeObject(Math.max(this.min, r.min), Math.min(this.max, r.max));

            return retObject;
        }

        @Override
        public String toString() {
            return Arrays.asList(min, max).toString();
        }

        @Override
        public int hashCode() {
            int hash = 3;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final RangeObject other = (RangeObject) obj;
            if (Double.doubleToLongBits(this.min) != Double.doubleToLongBits(other.min)) {
                return false;
            }
            if (Double.doubleToLongBits(this.max) != Double.doubleToLongBits(other.max)) {
                return false;
            }
            return true;
        }

        public static boolean combineCollide(RangeObject r1, RangeObject r2) {
            if (r1.collidesWith(r2) == false) {
                if (r1.min != r2.max && r2.min != r1.max) {
                    return false;
                }
            }
            return true;

        }

        public static RangeObject combine(RangeObject r1, RangeObject r2) {
            if (combineCollide(r1, r2) == false) {
                return EMPTY_SET;
            }
            return new RangeObject(Math.min(r1.min, r2.min), Math.max(r1.max, r2.max));
        }

        public static RangeObject[] combineRanges(RangeObject... ranges) {
//            System.out.println("RANGES:" + Arrays.deepToString(ranges));
            System.out.println(Arrays.toString(ranges));
            if (ranges.length <= 1) {
                return ranges;
            }
            List<RangeObject> retList = new ArrayList();
            //Check for collisions
            boolean collide = false;
            for (int INDEX = 0; INDEX < ranges.length - 1; INDEX++) {
//                    System.out.println("RANGE:" + ranges[INDEX]);
                for (int SUB_INDEX = INDEX + 1; SUB_INDEX < ranges.length; SUB_INDEX++) {
//                    System.out.println("SUBRANGE:" + ranges[SUB_INDEX]);
                    if (RangeObject.combineCollide(ranges[INDEX], ranges[SUB_INDEX])) {
                        collide = true;
                        RangeObject addElem = RangeObject.combine(ranges[INDEX], ranges[SUB_INDEX]);
                        retList.add(addElem);
//                        System.out.println("ADD:" + addElem);
                        continue;
                    }
                    if (retList.contains(ranges[SUB_INDEX]) == false && SUB_INDEX == ranges.length - 1) {
                        retList.add(ranges[SUB_INDEX]);
                    }
                }
            }
            if (collide == false) {
                return ranges;
            }
            System.out.println("RECURR");
            RangeObject[] tempArr = retList.toArray(new RangeObject[retList.size()]);
            System.out.println("TEMP ARR");
            retList = Arrays.asList(combineRanges(tempArr));
            System.out.println("END RECURR");
            return retList.toArray(new RangeObject[retList.size()]);
        }

        public RangeObject[] intersectionRemainder(RangeObject r) {

            if (this.collidesWith(r) == false) {
                System.out.println("DOESNT COLLIDE");
                return new RangeObject[]{this, r};
            }
            List<RangeObject> list = new ArrayList();
            list.addAll(Arrays.asList(leftRem(r), rightRem(r)));
            RangeObject[] retArray = list.toArray(new RangeObject[list.size()]);
            retArray = combineRanges(retArray);
            return retArray;
        }

        public RangeObject leftRem(RangeObject r) {

            if (this.collidesWith(r) == false) {
                if (this.min < r.min) {
                    return this;
                } else if (this.min == r.min) {
                    return EMPTY_SET;
                }
                return r;
            }

            RangeObject intersection = this.intersect(r);

            RangeObject left = new RangeObject(Math.min(this.min, r.min), intersection.min);
            if (left.min == left.max) {
                return EMPTY_SET;
            }
            return left;
        }

        public RangeObject rightRem(RangeObject r) {

            if (this.collidesWith(r) == false) {
                if (this.max > r.max) {
                    return this;
                } else if (this.max == r.max) {
                    return EMPTY_SET;
                }
                return r;
            }

            RangeObject intersection = this.intersect(r);

            RangeObject right = new RangeObject(intersection.max, Math.max(this.max, r.max));
            if (right.min == right.max) {
                return EMPTY_SET;
            }
            return right;
        }

        public static RangeObject[] intersectionRemainder_arr(RangeObject[] input) {
            RangeObject[] combinedArr = combineRanges(input);
            System.out.println("COMBINED");
            List<RangeObject> retList = new ArrayList();
            for (int INDEX = 0; INDEX < combinedArr.length - 1; INDEX++) {
                for (int SUB_INDEX = 0; SUB_INDEX < combinedArr.length; SUB_INDEX++) {
                    retList.addAll(Arrays.asList(combinedArr[INDEX].intersectionRemainder(combinedArr[SUB_INDEX])));
                }
            }
            int n = retList.size();
            System.out.println("LIST n:" + n);
            RangeObject[] retArray = new RangeObject[n];
            retArray = retList.toArray(retArray);
            System.out.println("TO ARRAY");
            RangeObject[] retRet = combineRanges(retArray);
            System.out.println("COMBINED");
            return retRet;
        }

        public static RangeObject[] domainFree(RangeObject domain, RangeObject... input) {
            List<RangeObject> retList = new ArrayList();
            RangeObject curr = input[0];
            if (domain.min < curr.min) {
                retList.add(new RangeObject(domain.min, curr.min));
            }
            for (int INDEX = 0; INDEX < input.length - 1; INDEX++) {
                curr = input[INDEX];
                RangeObject next = input[INDEX + 1];
                if (curr.max > domain.max) {
                    break;
                }
                if (curr.max < next.min) {
                    retList.add(new RangeObject(curr.max, next.min));
                    continue;
                }
            }
            curr = input[input.length - 1];
            if (curr.max < domain.max) {
                retList.add(new RangeObject(curr.max, domain.max));
            }
            RangeObject[] retArray = new RangeObject[retList.size()];
            retArray = retList.toArray(retArray);
            return retArray;
        }

    }

    private RangeFunctions() {

    }

    public static class Person {
    }

    public static class Schedule {

        public HashMap<DayName, Day> week;

        public Schedule() {
            init();
        }

        public void init() {
            this.week = new HashMap() {
                {
                    for (int i = 0; i < DayName.values().length; i++) {
                        this.put(DayName.values()[i], new Day());
                    }
                }
            };
        }

        public void addEvent(DayName name, Event e) {

        }

    }

    public static class Day {

        public enum DayName {

            SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;

            public int getNumber() {
                return Arrays.asList(DayName.values()).indexOf(this);
            }

        }

        public Event[] events;

        public Day(Event... events) {
            this.events = events;
        }

        public boolean collide(Event e) {
            boolean retValue = false;

            for (int i = 0; i < this.events.length; i++) {
                if (this.events[i].collide(e)) {
                    retValue = true;
                    break;
                }
            }

            return retValue;
        }

        public RangeObject[] intersect(Day d) {
            RangeObject[] retArray = new RangeObject[this.events.length];
            for (int index = 0; index < this.events.length; index++) {
                retArray[index] = d.intersect(this.events[index]);
            }

            return retArray;
        }

        public RangeObject intersect(Event e) {
            for (int i = 0; i < this.events.length; i++) {
                RangeObject intersection = this.events[i].intersect(e);
                if (intersection.equals(RangeObject.EMPTY_SET) == true) {
                    // No intersection occured.
                    continue;
                } //implied else
                return intersection;
            }
            return RangeObject.EMPTY_SET;
        }

    }

    public static class Event {

        public RangeObject range;

        public Event(RangeObject range) {
            this.range = range;
        }

        public boolean collide(Event e) {
            return RangeObject.collide(range, e.range);
        }

        public RangeObject intersect(Event e) {
            return this.range.intersect(e.range);
        }

    }

    public static void main(String[] args) {
        testAlgorithmRange();
    }

    public static void testRangeObject() {
        RangeObject r1 = new RangeObject(0, 5),
                r2 = new RangeObject(3, 8),
                r3 = new RangeObject(12, 14);
        System.out.printf("r1:%s\t=\tr2:%s\t:%s\n", r1, r2, RangeObject.collide(r1, r2));
        System.out.println("intersection:" + r1.intersect(r2));
        System.out.println("leftRem:" + r1.leftRem(r2) + "\trightRem:" + r1.rightRem(r2));

        RangeObject day = new RangeObject(0, 24);
        System.out.println("domainFree:" + Arrays.toString(RangeObject.domainFree(day, r1, r2, r3)));
    }

    public static void testAlgorithmRange() {
        RangeObject r1 = new RangeObject(0, 5),
                r2 = new RangeObject(3, 8),
                r3 = new RangeObject(12, 14),
                r4 = new RangeObject(0, 10),
                r5 = new RangeObject(16, 20);
        RangeObject[] person1 = new RangeObject[]{r1, r2, r3}, person2 = new RangeObject[]{r4, r5};
        System.out.printf("person1:%s\nperson2:%s\n", Arrays.toString(person1), Arrays.toString(person2));
        RangeObject[] freeTime = freeTimeAlgorithm(person1, person2);
        System.out.println("freeTime:" + Arrays.toString(freeTime));
        RangeObject[] freeTime1 = freeTimeAlgorithm(person1, freeTime);
        System.out.println("freeTime for person 1 after hanging with person 2:\n\t" + Arrays.toString(freeTime1));
        RangeObject[] freeTime2 = freeTimeAlgorithm(person2, freeTime);
        System.out.println("freeTime for person 2 after hanging with person 1:\n\t" + Arrays.toString(freeTime2));

    }

    private static RangeObject[] freeTimeAlgorithm(RangeObject[] person1, RangeObject[] person2) {
        final int FREE = 0, BUSY = 1;
        int[] combined_schedule = new int[24 * 60];
        for (int i = 0; i < combined_schedule.length; i++) {
            combined_schedule[i] = FREE;
        }
        for (int INDEX = 0; INDEX < person1.length; INDEX++) {
            RangeObject curr = person1[INDEX];
            for (int SUBINDEX = (int) (curr.min * 60); SUBINDEX < (int) (curr.max * 60); SUBINDEX++) {
                combined_schedule[SUBINDEX] = BUSY;
            }
        }
        for (int INDEX = 0; INDEX < person2.length; INDEX++) {
            RangeObject curr = person2[INDEX];
            for (int SUBINDEX = (int) (curr.min * 60); SUBINDEX < (int) (curr.max * 60); SUBINDEX++) {
                combined_schedule[SUBINDEX] = BUSY;
            }
        }
        List<RangeObject> freeTime = new ArrayList(), busyTime = new ArrayList();
        int prevVal = combined_schedule[0];
        int minIndex = 0;
        int maxIndex = 0;
        for (int INDEX = 0; INDEX < combined_schedule.length; INDEX++) {
            int currVal = combined_schedule[INDEX];
            if (currVal == prevVal) {
                prevVal = currVal;
                continue;
            }
            maxIndex = INDEX;
            double minHours = minIndex / 60.0;
            double maxHours = maxIndex / 60.0;
            RangeObject tempObject = new RangeObject(minHours, maxHours);
            switch (prevVal) {
                case FREE:
                    freeTime.add(tempObject);
                    break;
                case BUSY:
                    busyTime.add(tempObject);
                    break;
                default:
            }

            minIndex = INDEX;
            prevVal = currVal;
        }
        RangeObject[] retArray = new RangeObject[freeTime.size()];
        retArray = freeTime.toArray(retArray);
        return retArray;
    }

    public static class ArrayAlgorithmRanges {

        private ArrayAlgorithmRanges() {

        }
        /*
            takes a range -> array of time, puts on total time, puts other on total time, counts zeros and converts back to ranges
            
        
         */

    }

}
