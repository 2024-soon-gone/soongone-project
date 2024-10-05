import React from 'react';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { View, Button, Text } from 'react-native';
import Icon from 'react-native-vector-icons/MaterialIcons';
import Home from '../assets/icon/home';
import Search from '../assets/icon/search';
import Plus from '../assets/icon/plus-outline';
import Deal from '../assets/icon/deal';
import Person from '../assets/icon/person';
import theme from '../assets/Theme';
import HomeScreen from './Home/Home';

const Tab = createBottomTabNavigator();

function Main() {
  return (
    <Tab.Navigator
      initialRouteName="Home"
      screenOptions={{
        tabBarActiveTintColor: theme.black,
        tabBarInactiveTintColor: theme.grey3,
        tabBarShowLabel: true,
        headerShown: false,
        tabBarStyle: {
          paddingBottom: '12px',
          height: '83px',
        },
        tabBarItemStyle: {
          height: '49px',
          paddingTop: '7px',
          bottom: 0,
        },
        tabBarIconStyle: {
          marginBottom: '3.87px',
        },
      }}
    >
      <Tab.Screen
        name="Home"
        component={HomeScreen}
        options={{
          title: '홈',
          tabBarIcon: ({ color, size }) => (
            <Home stroke={color} strokeWidth="2" strokeLinejoin="round" />
          ),
        }}
      />
      <Tab.Screen
        name="Search"
        component={SearchScreen}
        options={{
          title: '검색',
          tabBarIcon: ({ color, size }) => (
            <Search stroke={color} strokeWidth="2" strokeLinejoin="round" />
          ),
        }}
      />
      <Tab.Screen
        name="Camera"
        component={CameraScreen}
        options={{
          title: '추가하기',
          tabBarIcon: ({ color, size }) => (
            <Plus stroke={color} strokeWidth="2" strokeLinejoin="round" />
          ),
        }}
      />
      <Tab.Screen
        name="Transaction"
        component={TranscationScreen}
        options={{
          title: '거래',
          tabBarIcon: ({ color, size }) => (
            <Deal stroke={color} strokeWidth="2" strokeLinejoin="round" />
          ),
        }}
      />
      <Tab.Screen
        name="Profile"
        component={ProfileScreen}
        options={{
          title: '마이페이지',
          tabBarIcon: ({ color, size }) => (
            <Person stroke={color} strokeWidth="2" strokeLinejoin="round" />
          ),
        }}
      />
    </Tab.Navigator>
  );
}

function SearchScreen() {
  return <Text>Search</Text>;
}

function CameraScreen() {
  return <Text>Camera</Text>;
}

function TranscationScreen() {
  return <Text>Transaction</Text>;
}

function ProfileScreen() {
  return <Text>Profile</Text>;
}

export default Main;
