import 'react-native-gesture-handler';
import { NavigationContainer } from '@react-navigation/native';
import { createStackNavigator } from '@react-navigation/stack';
import Login from './screens/Login';
import Onboarding from './screens/Onboarding';
import Main from './screens/Main';
import InitUserInfo from './screens/InitUserInfo/InitUserInfo';

export default function App() {
  const Stack = createStackNavigator();
  return (
    <NavigationContainer>
      <Stack.Navigator
        initialRouteName="InitUserInfo"
        screenOptions={{
          headerShown: true,
          headerTitleAlign: 'center',
        }}
      >
        <Stack.Screen name="Login" component={Login} />
        <Stack.Screen name="Onboarding" component={Onboarding} />
        <Stack.Screen name="Main" component={Main} />
        <Stack.Screen name="InitUserInfo" component={InitUserInfo} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}
