package com.example.storm.benchmark.produce.scooter.bean;

public class Bats {
        private int capacity;
        private int voltage;
        private int nominalVoltage;
        private int current;
        private String fault;
        private int portNumber;
        private int cycle;
        private String id;
        private int soc;
        private String damage;
        private int temperature;
        private int nominalCurrent;
        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }
        public int getCapacity() {
            return capacity;
        }

        public void setVoltage(int voltage) {
            this.voltage = voltage;
        }
        public int getVoltage() {
            return voltage;
        }

        public void setNominalVoltage(int nominalVoltage) {
            this.nominalVoltage = nominalVoltage;
        }
        public int getNominalVoltage() {
            return nominalVoltage;
        }

        public void setCurrent(int current) {
            this.current = current;
        }
        public int getCurrent() {
            return current;
        }

        public void setFault(String fault) {
            this.fault = fault;
        }
        public String getFault() {
            return fault;
        }

        public void setPortNumber(int portNumber) {
            this.portNumber = portNumber;
        }
        public int getPortNumber() {
            return portNumber;
        }

        public void setCycle(int cycle) {
            this.cycle = cycle;
        }
        public int getCycle() {
            return cycle;
        }

        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }

        public void setSoc(int soc) {
            this.soc = soc;
        }
        public int getSoc() {
            return soc;
        }

        public void setDamage(String damage) {
            this.damage = damage;
        }
        public String getDamage() {
            return damage;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }
        public int getTemperature() {
            return temperature;
        }

        public void setNominalCurrent(int nominalCurrent) {
            this.nominalCurrent = nominalCurrent;
        }
        public int getNominalCurrent() {
            return nominalCurrent;
        }

    @Override
    public String toString() {
        return "Bats{" +
                "capacity=" + capacity +
                ", voltage=" + voltage +
                ", nominalVoltage=" + nominalVoltage +
                ", current=" + current +
                ", fault='" + fault + '\'' +
                ", portNumber=" + portNumber +
                ", cycle=" + cycle +
                ", id='" + id + '\'' +
                ", soc=" + soc +
                ", damage='" + damage + '\'' +
                ", temperature=" + temperature +
                ", nominalCurrent=" + nominalCurrent +
                '}';
    }
}
